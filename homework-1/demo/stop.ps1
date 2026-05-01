[CmdletBinding()]
param()

$ErrorActionPreference = 'Stop'

. (Join-Path $PSScriptRoot 'AppLifecycle.ps1')

$result = Stop-ManagedApp

switch ($result.Reason) {
    'missing-state' {
        Write-Host 'Banking Transactions API is not running.'
    }
    'stale-state' {
        Write-Host 'Removed stale Banking Transactions API state.'
    }
    default {
        Write-Host "Stopped Banking Transactions API (PID $($result.Pid))."
    }
}
