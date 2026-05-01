$helperPath = Join-Path $PSScriptRoot 'AppLifecycle.ps1'

. $helperPath

Describe 'AppLifecycle helper' {
    It 'builds managed paths under the project target directory' {
        $paths = Get-ManagedAppPaths

        $paths.ProjectRoot | Should Match 'homework-1$'
        $paths.TargetDir | Should Match 'homework-1\\target$'
        $paths.StateFile | Should Match 'homework-1\\target\\managed-app.json$'
    }

    It 'round-trips managed process state to disk' {
        $stateFile = Join-Path $TestDrive 'managed-app.json'
        $expected = [pscustomobject]@{
            Pid = 4242
            ProcessStartTimeUtc = '2026-04-23T16:00:00.0000000Z'
            Port = 8080
        }

        Write-ManagedAppState -StateFile $stateFile -State $expected
        $actual = Read-ManagedAppState -StateFile $stateFile

        $actual.Pid | Should Be 4242
        $actual.ProcessStartTimeUtc | Should Be '2026-04-23T16:00:00.0000000Z'
        $actual.Port | Should Be 8080
    }

    It 'treats cleared state as missing on the next read' {
        $stateFile = Join-Path $TestDrive 'managed-app.json'
        $state = [pscustomobject]@{
            Pid = 4242
            ProcessStartTimeUtc = '2026-04-23T16:00:00.0000000Z'
            Port = 8080
        }

        Write-ManagedAppState -StateFile $stateFile -State $state
        Set-Content -LiteralPath $stateFile -Value '{"Inactive":true}' -Encoding UTF8

        $actual = Read-ManagedAppState -StateFile $stateFile

        $actual | Should Be $null
    }

    It 'treats an empty JSON object as missing state' {
        $stateFile = Join-Path $TestDrive 'managed-app.json'

        Set-Content -LiteralPath $stateFile -Value '{}' -Encoding UTF8

        $actual = Read-ManagedAppState -StateFile $stateFile

        $actual | Should Be $null
    }

    It 'recognizes a matching live process' {
        $process = Get-Process -Id $PID
        $state = [pscustomobject]@{
            Pid = $PID
            ProcessStartTimeUtc = $process.StartTime.ToUniversalTime().ToString('o')
            Port = 8080
        }

        Test-ManagedProcessState -State $state | Should Be $true
    }

    It 'rejects stale process metadata' {
        $state = [pscustomobject]@{
            Pid = $PID
            ProcessStartTimeUtc = '2000-01-01T00:00:00.0000000Z'
            Port = 8080
        }

        Test-ManagedProcessState -State $state | Should Be $false
    }
}
