$ErrorActionPreference = "Stop"
$Root = Split-Path -Parent $PSScriptRoot
$State = Join-Path $Root "target/managed-app.json"
if (!(Test-Path $State)) {
    Write-Host "No managed Support Ticket API process recorded."
    exit 0
}
$Existing = Get-Content $State | ConvertFrom-Json
$Process = Get-Process -Id $Existing.pid -ErrorAction SilentlyContinue
if ($Process) {
    Stop-Process -Id $Existing.pid -Force
    Write-Host "Stopped Support Ticket API PID $($Existing.pid)"
} else {
    Write-Host "Recorded process PID $($Existing.pid) is not running."
}
$Listeners = Get-NetTCPConnection -LocalPort 8080 -State Listen -ErrorAction SilentlyContinue
foreach ($Listener in $Listeners) {
    $PortProcess = Get-Process -Id $Listener.OwningProcess -ErrorAction SilentlyContinue
    if ($PortProcess) {
        Stop-Process -Id $Listener.OwningProcess -Force
        Write-Host "Stopped process PID $($Listener.OwningProcess) listening on port 8080"
    }
}
Remove-Item -Path $State -Force
