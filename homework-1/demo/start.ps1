[CmdletBinding()]
param(
    [switch]$SkipBuild,
    [int]$StartupTimeoutSeconds = 30
)

$ErrorActionPreference = 'Stop'

. (Join-Path $PSScriptRoot 'AppLifecycle.ps1')

$result = Start-ManagedApp -SkipBuild:$SkipBuild -StartupTimeoutSeconds $StartupTimeoutSeconds

Write-Host "Started Banking Transactions API (PID $($result.Pid)) at $($result.Url)."
