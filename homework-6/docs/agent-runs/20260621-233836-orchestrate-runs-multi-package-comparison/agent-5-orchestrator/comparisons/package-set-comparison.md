# Package-Set Comparison

## Compared Packages

| Package | Stack | Status | Primary Clio evidence |
|---|---|---|---|
| Canonical selected package | Python | Canonical | `20260621-011348-generate-docs-python-review-repair` |
| Latest Python package | Python | Preserved candidate, not selected | `20260621-225923-generate-docs-python-hera-python-full-set` |
| Latest Java package | Java | Preserved candidate, not selected | `20260621-203431-generate-docs-java-hera-java-full-set` |

The comparison is based primarily on Clio (Documentation Generator) output, as authorized by the operator.

## Summary Matrix

| Criterion | Canonical selected Python | Latest Python candidate | Latest Java candidate |
|---|---|---|---|
| Selection state | Selected canonical package | Candidate only | Candidate only |
| Full-set traceability | Strong, recorded in `selection-sets.json` and `final-selection.md` | Strong, Hera-dispatched full set with named child runs | Strong, Hera-dispatched full set with named child runs and usable Themis retry |
| Runtime components documented | Integrator, Transaction Validator, Fraud Detector, Settlement Processor, MCP reader | Integrator, Transaction Validator, Fraud Detector, Settlement Processor, Reporting Agent, MCP reader | Integrator, TransactionValidator, FraudDetector, SettlementProcessor, ReportingAgent, MCP reader compatibility |
| Pipeline evidence | 8 total, 2 settled, 2 rejected, 4 review-required, 0 error | Same safe counts | Same safe counts with `complete=true` |
| Test evidence | 50 tests | 36 tests | 13 tests |
| Coverage evidence | 94.79% | 97.44% | 87.86% JaCoCo instruction coverage |
| Coverage gate | 80% pass, 99% expected fail | 80% pass, 99% expected fail | 80% pass, 99% expected fail |
| Screenshot evidence | Selected stable screenshots; semantically distinct after repair | Fresh run-local terminal-style screenshots | Fresh run-local terminal-style Java screenshots |
| MCP evidence | Context7 plus custom `pipeline-status` represented in selected docs | Context7 plus custom helper evidence, with root MCP subprocess limitation documented | Context7 plus Python `pipeline-status` reader compatibility, with staging/helper limitation documented |
| Environment limitations | Windows sandbox coverage-file rename issue required unsandboxed coverage gate | Direct run-local hook shell blocked; MCP subprocess reads root results by default | Maven empty-settings workaround; Clio did not rerun long Maven commands |
| Selection risk | Lowest, already canonical | Medium-low, same stack but canonical replacement needed | Higher, stack switch or alternate registration needed |

## Findings

### Canonical Selected Python

The canonical package is the safest current submission target because it is already selected, registered, and copied to canonical root files. Its Clio evidence is complete and repair-focused: 50 tests pass, the 80% coverage gate passes at 94.79%, required screenshots are semantically distinct, and selection records name the full spec/code/tests/docs chain.

Its main weakness is freshness. It documents the three-component runtime package without the later Reporting Agent, and it predates the newer Hera-generated full-set evidence. That is not a compliance blocker because the assignment minimum is at least three cooperating runtime components.

### Latest Python Candidate

The latest Python candidate is the strongest upgrade candidate if the operator wants to replace the canonical package while staying on Python. It adds a Reporting Agent, has a higher documented coverage result at 97.44%, preserves first-level Hera child dispatch evidence, and keeps the same safe pipeline outcome counts.

Its selection risk is procedural rather than technical. The candidate is not selected, its spec fingerprint differs from the canonical spec by design, and the latest Python run folders were untracked before this comparison. A future selection would need explicit Hera `select-set` authorization and inventory-driven canonical copy.

### Latest Java Candidate

The Java candidate is a viable preserved alternate. It has a complete Java-specific spec/code/tests/docs chain, Maven/Jackson/JUnit/JaCoCo documentation, BigDecimal money handling, passing Java pipeline evidence, validation-only evidence, and an 80% coverage gate pass at 87.86%.

Its main tradeoffs are selection friction and environment dependence. It uses a Themis retry because the first Java Themis attempt is blocked and non-selectable, it needed empty Maven settings in this environment, and the existing Python MCP reader can inspect Java result shapes only when pointed at candidate results or after staging selected Java results under root `shared/results`.

## Recommendation

Keep `python-canonical-20260621` canonical for immediate submission stability.

If the operator wants to improve the canonical package before final submission, select the latest Python candidate rather than Java. It is same-stack, Hera-generated, includes the fourth Reporting Agent, and has the strongest documented coverage while preserving the expected result contract.

Register the Java package as an alternate only if the assignment narrative benefits from demonstrating stack portability. Do not replace the canonical Python set with Java unless the operator explicitly wants a Java canonical submission and accepts the Maven settings and MCP staging implications.

## Canonical Output Status

Canonical output remained unchanged by this compare-set run. No root product files, selected docs, stable screenshots, MCP files, final-selection records, or selection registry entries were edited.

