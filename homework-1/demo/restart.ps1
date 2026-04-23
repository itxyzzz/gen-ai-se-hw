[CmdletBinding()]
param(
    [switch]$SkipBuild,
    [int]$StartupTimeoutSeconds = 30
)

$ErrorActionPreference = 'Stop'

. (Join-Path $PSScriptRoot 'AppLifecycle.ps1')

[void](Stop-ManagedApp -IgnoreMissing)
$result = Start-ManagedApp -SkipBuild:$SkipBuild -StartupTimeoutSeconds $StartupTimeoutSeconds

Write-Host "Restarted Banking Transactions API (PID $($result.Pid)) at $($result.Url)."
