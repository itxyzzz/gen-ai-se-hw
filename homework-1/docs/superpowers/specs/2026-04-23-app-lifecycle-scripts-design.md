# App Lifecycle Scripts Design

**Date:** 2026-04-23

## Goal

Provide a reliable local workflow to start, stop, and restart the Banking Transactions API without depending on Spring Boot's fat-JAR repackaging step, which is currently blocked by an intermittently locked file in `target/`.

## Recommended Approach

Add three PowerShell entrypoint scripts under `demo/`:

- `start.ps1`
- `stop.ps1`
- `restart.ps1`

Back them with a shared helper script that owns process state, build commands, and readiness checks.

## Design

### Build and launch path

`start.ps1` will build the latest compiled application output with:

```powershell
mvn -q -DskipTests compile dependency:copy-dependencies -DincludeScope=runtime
```

Then it will launch the Spring Boot main class from:

- `target/classes`
- `target/dependency/*`

This avoids the locked `target\banking-transactions-api-0.0.1-SNAPSHOT.jar` rename path entirely.

### Managed process state

The scripts will keep a small JSON state file under `target/` that records:

- PID
- process start time in UTC
- managed port

`stop.ps1` will only kill the process when both the PID and recorded start time match, which protects against PID reuse.

### Readiness and safety

- `start.ps1` will refuse to start a second managed instance when the recorded process is still alive.
- `start.ps1` will wait for `http://localhost:8080/transactions` to return `200`.
- `stop.ps1` will remove stale state when the recorded process no longer exists.
- `restart.ps1` will call `stop.ps1` and then `start.ps1`.

## Testing

Add a small Pester test file for the shared helper logic:

- managed path calculation
- state file round-trip
- process identity validation with PID + start time

Runtime verification will also cover:

- `start.ps1`
- `stop.ps1`
- `restart.ps1`

## Documentation

Update `README.md` and `HOWTORUN.md` to promote the new scripts as the primary Windows workflow.
