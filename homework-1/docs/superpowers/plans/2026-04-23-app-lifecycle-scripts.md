# App Lifecycle Scripts Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add durable local start, stop, and restart scripts that always run the latest compiled Banking Transactions API build without relying on the locked fat-JAR path.

**Architecture:** Keep process-management logic in a shared PowerShell helper under `demo/`, then use thin entrypoint scripts for `start`, `stop`, and `restart`. Persist managed process identity under `target/` and verify readiness against the running HTTP endpoint.

**Tech Stack:** PowerShell 5, Maven, Java 17, Pester 3.

---

## File Structure

- Create: `demo/AppLifecycle.ps1`
- Create: `demo/AppLifecycle.Tests.ps1`
- Create: `demo/start.ps1`
- Create: `demo/stop.ps1`
- Create: `demo/restart.ps1`
- Modify: `demo/run.bat`
- Modify: `README.md`
- Modify: `HOWTORUN.md`
- Create: `docs/superpowers/specs/2026-04-23-app-lifecycle-scripts-design.md`

### Task 1: Write the failing script tests

**Files:**
- Create: `demo/AppLifecycle.Tests.ps1`

- [ ] **Step 1: Write failing Pester tests**

Add tests that expect a shared lifecycle helper script to provide:

- `Get-ManagedAppPaths`
- `Write-ManagedAppState`
- `Read-ManagedAppState`
- `Remove-ManagedAppState`
- `Test-ManagedProcessState`

- [ ] **Step 2: Run the tests to verify failure**

Run:

```powershell
Invoke-Pester .\demo\AppLifecycle.Tests.ps1
```

Expected: failure because `demo/AppLifecycle.ps1` does not exist yet.

### Task 2: Implement shared lifecycle helper

**Files:**
- Create: `demo/AppLifecycle.ps1`

- [ ] **Step 1: Add path helpers**
- [ ] **Step 2: Add state file read/write/remove helpers**
- [ ] **Step 3: Add process identity validation using PID + start time**
- [ ] **Step 4: Add build, launch, readiness, and stop functions**
- [ ] **Step 5: Re-run Pester tests**

### Task 3: Add entrypoint scripts

**Files:**
- Create: `demo/start.ps1`
- Create: `demo/stop.ps1`
- Create: `demo/restart.ps1`
- Modify: `demo/run.bat`

- [ ] **Step 1: Keep each script thin and route through the shared helper**
- [ ] **Step 2: Keep `run.bat` compatible by delegating to `start.ps1`**
- [ ] **Step 3: Verify the scripts can be called directly from `demo/`**

### Task 4: Update docs and verify end to end

**Files:**
- Modify: `README.md`
- Modify: `HOWTORUN.md`

- [ ] **Step 1: Document the new preferred Windows workflow**
- [ ] **Step 2: Run `start.ps1` and verify HTTP `200` from `/transactions`**
- [ ] **Step 3: Run `stop.ps1` and verify the app is down**
- [ ] **Step 4: Run `restart.ps1` and verify the app is back up**

## Self-Review

- Spec coverage: build, launch, managed state, safety, readiness, testing, and docs are all represented.
- Placeholder scan: the tasks call out concrete files, commands, and expected outcomes.
- Type consistency: helper names in the test task match the planned implementation names.
