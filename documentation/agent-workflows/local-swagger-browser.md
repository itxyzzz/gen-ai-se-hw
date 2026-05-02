# Local Swagger Browser Workflow

Use this workflow when opening a local Swagger UI from Codex Desktop with browser-use.

## Probe First

Check whether the API is already serving OpenAPI before starting anything:

```powershell
Invoke-WebRequest http://localhost:8080/v3/api-docs -UseBasicParsing -TimeoutSec 5
```

If the response is `200`, skip app startup and open Swagger UI directly:

```text
http://localhost:8080/api-docs
```

## Start Only When Needed

If the probe fails, start the app without blocking the browser step, then poll `/v3/api-docs` with a short timeout until it returns `200`.

Do not run a long foreground start command and wait indefinitely before opening the browser. The intended sequence is:

1. Probe `/v3/api-docs`.
2. Start the app only if the probe fails.
3. Poll `/v3/api-docs` until ready.
4. Open `/api-docs` with browser-use.

## Browser-Use Runtime Check

If browser-use or `node_repl` reports that Node is too old, verify the active Node version:

```powershell
node --version
```

Browser-use requires Node `>=22.22.0`. On this machine, use the NVM-managed Node 24 runtime:

```powershell
nvm use 24.14.0
```

Then retry browser-use. If the Codex process still sees the old Node runtime, restart Codex Desktop and retry the probe-first workflow.
