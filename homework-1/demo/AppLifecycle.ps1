Set-StrictMode -Version Latest

function Get-ManagedAppPaths {
    [CmdletBinding()]
    param(
        [string]$ProjectRoot = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
    )

    $targetDir = Join-Path $ProjectRoot 'target'

    [pscustomobject]@{
        ProjectRoot = $ProjectRoot
        TargetDir = $targetDir
        StateFile = Join-Path $targetDir 'managed-app.json'
        MainClass = 'com.setu.banking.BankingTransactionsApplication'
        Url = 'http://localhost:8080/transactions'
        Port = 8080
    }
}

function Read-ManagedAppState {
    [CmdletBinding()]
    param(
        [string]$StateFile = (Get-ManagedAppPaths).StateFile
    )

    if (-not (Test-Path -LiteralPath $StateFile)) {
        return $null
    }

    $raw = Get-Content -LiteralPath $StateFile -Raw

    if ([string]::IsNullOrWhiteSpace($raw)) {
        return $null
    }

    $state = $raw | ConvertFrom-Json

    $propertyNames = @($state.PSObject.Properties | ForEach-Object { $_.Name })

    if ($propertyNames.Count -eq 0) {
        return $null
    }

    if ($propertyNames -contains 'Inactive' -and $state.Inactive) {
        return $null
    }

    $state
}

function Write-ManagedAppState {
    [CmdletBinding()]
    param(
        [Parameter(Mandatory = $true)]
        [string]$StateFile,
        [Parameter(Mandatory = $true)]
        [psobject]$State
    )

    $directory = Split-Path -Parent $StateFile

    if (-not (Test-Path -LiteralPath $directory)) {
        New-Item -ItemType Directory -Path $directory -Force | Out-Null
    }

    $State | ConvertTo-Json | Set-Content -LiteralPath $StateFile -Encoding UTF8
}

function Remove-ManagedAppState {
    [CmdletBinding()]
    param(
        [string]$StateFile = (Get-ManagedAppPaths).StateFile
    )

    if (-not (Test-Path -LiteralPath $StateFile)) {
        return
    }

    [pscustomobject]@{
        Inactive = $true
    } | ConvertTo-Json | Set-Content -LiteralPath $StateFile -Encoding UTF8
}

function Test-ManagedProcessState {
    [CmdletBinding()]
    param(
        [Parameter(Mandatory = $true)]
        [psobject]$State
    )

    if (-not $State.Pid -or -not $State.ProcessStartTimeUtc) {
        return $false
    }

    $process = Get-Process -Id ([int]$State.Pid) -ErrorAction SilentlyContinue

    if (-not $process) {
        return $false
    }

    $process.StartTime.ToUniversalTime().ToString('o') -eq [string]$State.ProcessStartTimeUtc
}

function Test-AppEndpointReachable {
    [CmdletBinding()]
    param(
        [string]$Url = (Get-ManagedAppPaths).Url
    )

    try {
        Invoke-WebRequest -UseBasicParsing -Uri $Url -TimeoutSec 3 | Out-Null
        return $true
    } catch [System.Net.WebException] {
        if ($_.Exception.Response) {
            return $true
        }

        return $false
    } catch {
        return $false
    }
}

function Test-ManagedAppHealthy {
    [CmdletBinding()]
    param(
        [string]$Url = (Get-ManagedAppPaths).Url
    )

    try {
        $response = Invoke-WebRequest -UseBasicParsing -Uri $Url -TimeoutSec 3
        return $response.StatusCode -eq 200
    } catch {
        return $false
    }
}

function Invoke-AppBuild {
    [CmdletBinding()]
    param(
        [string]$ProjectRoot = (Get-ManagedAppPaths).ProjectRoot
    )

    Push-Location $ProjectRoot

    try {
        & mvn '-q' '-DskipTests' 'compile' 'dependency:copy-dependencies' '-DincludeScope=runtime'

        if ($LASTEXITCODE -ne 0) {
            throw 'Maven build failed.'
        }
    } finally {
        Pop-Location
    }
}

function Wait-ManagedAppReady {
    [CmdletBinding()]
    param(
        [Parameter(Mandatory = $true)]
        [System.Diagnostics.Process]$Process,
        [int]$StartupTimeoutSeconds = 30,
        [string]$Url = (Get-ManagedAppPaths).Url
    )

    $deadline = (Get-Date).AddSeconds($StartupTimeoutSeconds)

    while ((Get-Date) -lt $deadline) {
        if (Test-ManagedAppHealthy -Url $Url) {
            return $true
        }

        $Process.Refresh()

        if ($Process.HasExited) {
            throw "Banking Transactions API exited before becoming ready. Exit code: $($Process.ExitCode)."
        }

        Start-Sleep -Seconds 1
    }

    return $false
}

function Start-ManagedApp {
    [CmdletBinding()]
    param(
        [switch]$SkipBuild,
        [int]$StartupTimeoutSeconds = 30,
        [string]$ProjectRoot = (Get-ManagedAppPaths).ProjectRoot
    )

    $paths = Get-ManagedAppPaths -ProjectRoot $ProjectRoot
    $state = Read-ManagedAppState -StateFile $paths.StateFile

    if ($state -and (Test-ManagedProcessState -State $state)) {
        throw "Banking Transactions API is already running with PID $($state.Pid)."
    }

    if ($state) {
        Remove-ManagedAppState -StateFile $paths.StateFile
    }

    if (Test-AppEndpointReachable -Url $paths.Url) {
        throw "Port $($paths.Port) already has a responding service. Stop it before starting the managed Banking Transactions API."
    }

    if (-not $SkipBuild) {
        Invoke-AppBuild -ProjectRoot $paths.ProjectRoot
    }

    $java = (Get-Command java -ErrorAction Stop).Source
    $arguments = '-cp "target\classes;target\dependency\*" ' + $paths.MainClass
    $process = Start-Process -FilePath $java -ArgumentList $arguments -WorkingDirectory $paths.ProjectRoot -PassThru
    $recordedProcess = Get-Process -Id $process.Id -ErrorAction Stop
    $newState = [pscustomobject]@{
        Pid = $process.Id
        ProcessStartTimeUtc = $recordedProcess.StartTime.ToUniversalTime().ToString('o')
        Port = $paths.Port
    }

    Write-ManagedAppState -StateFile $paths.StateFile -State $newState

    try {
        if (-not (Wait-ManagedAppReady -Process $process -StartupTimeoutSeconds $StartupTimeoutSeconds -Url $paths.Url)) {
            throw "Banking Transactions API did not become ready within $StartupTimeoutSeconds seconds."
        }
    } catch {
        if (Test-ManagedProcessState -State $newState) {
            Stop-Process -Id ([int]$newState.Pid) -Force -ErrorAction SilentlyContinue
        }

        Remove-ManagedAppState -StateFile $paths.StateFile
        throw
    }

    [pscustomobject]@{
        Pid = $process.Id
        Url = $paths.Url
        StateFile = $paths.StateFile
    }
}

function Stop-ManagedApp {
    [CmdletBinding()]
    param(
        [switch]$IgnoreMissing,
        [string]$ProjectRoot = (Get-ManagedAppPaths).ProjectRoot
    )

    $paths = Get-ManagedAppPaths -ProjectRoot $ProjectRoot
    $state = Read-ManagedAppState -StateFile $paths.StateFile

    if (-not $state) {
        return [pscustomobject]@{
            Stopped = $false
            Reason = 'missing-state'
        }
    }

    if (-not (Test-ManagedProcessState -State $state)) {
        Remove-ManagedAppState -StateFile $paths.StateFile

        return [pscustomobject]@{
            Stopped = $false
            Reason = 'stale-state'
        }
    }

    Stop-Process -Id ([int]$state.Pid) -Force -ErrorAction Stop

    $deadline = (Get-Date).AddSeconds(10)

    while ((Get-Date) -lt $deadline) {
        if (-not (Get-Process -Id ([int]$state.Pid) -ErrorAction SilentlyContinue)) {
            break
        }

        Start-Sleep -Milliseconds 500
    }

    Remove-ManagedAppState -StateFile $paths.StateFile

    [pscustomobject]@{
        Stopped = $true
        Reason = 'stopped'
        Pid = [int]$state.Pid
    }
}
