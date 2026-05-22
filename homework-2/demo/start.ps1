$ErrorActionPreference = "Stop"
$Root = Split-Path -Parent $PSScriptRoot
$Target = Join-Path $Root "target"
$State = Join-Path $Target "managed-app.json"
$Jar = Join-Path $Target "support-ticket-import-api-0.0.1-SNAPSHOT.jar"
New-Item -ItemType Directory -Force -Path $Target | Out-Null
if (Test-Path $State) {
    $Existing = Get-Content $State | ConvertFrom-Json
    $Process = Get-Process -Id $Existing.pid -ErrorAction SilentlyContinue
    if ($Process) {
        Write-Host "Support Ticket API already running on PID $($Existing.pid)"
        exit 0
    }
}
$Listener = Get-NetTCPConnection -LocalPort 8080 -State Listen -ErrorAction SilentlyContinue | Select-Object -First 1
if ($Listener) {
    @{ pid = $Listener.OwningProcess; url = "http://localhost:8080"; started_at = (Get-Date).ToString("o") } | ConvertTo-Json | Set-Content -Path $State
    Write-Host "Support Ticket API already appears to be running on port 8080 with PID $($Listener.OwningProcess)"
    exit 0
}
if (!(Test-Path $Jar)) {
    Write-Host "Building runnable jar..."
    & mvn -f (Join-Path $Root "pom.xml") -DskipTests package
}
$Out = Join-Path $Target "app.out.log"
$Err = Join-Path $Target "app.err.log"
$Process = Start-Process -FilePath "java" -ArgumentList "-jar", $Jar, "--server.port=8080" -WorkingDirectory $Root -RedirectStandardOutput $Out -RedirectStandardError $Err -PassThru -WindowStyle Hidden
@{ pid = $Process.Id; url = "http://localhost:8080"; started_at = (Get-Date).ToString("o") } | ConvertTo-Json | Set-Content -Path $State
Write-Host "Support Ticket API starting on http://localhost:8080 with PID $($Process.Id)"
Write-Host "Logs: $Out and $Err"
