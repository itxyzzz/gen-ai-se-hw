# Stack Profiles

Use this reference before Athena (Spec Writer) generates stack-specific files, functions, commands, tests, coverage tooling notes, or MCP-readable result notes for Homework 6.

These stack profiles are Athena (Spec Writer) generation controls. They are not generated transaction-system product requirements by themselves, and they must not cause `specification.md` to copy run-preservation, final-selection, or operator setup mechanics into product low-level tasks.

## Fixed Enum

| Stack value | Default? | Intended use |
|---|---:|---|
| `python` | Yes | Primary Homework 6 pipeline generation run. |
| `java` | No | Optional alternate generation run if time allows comparison. |

Supported values are exactly `python` and `java`.

## Selection Rules

- Omitted stack input means `stack=python`.
- `stack=python` uses the Python profile below.
- `stack=java` uses the Java profile below.
- `stack=auto` is unsupported.
- Any other value is unsupported.
- The run ID must include the selected stack.
- The generated `specification.md` must be stack-specific after selection. Domain goals may stay portable, but low-level tasks, file paths, functions, commands, tests, coverage tooling notes, and MCP notes must match the selected profile.

## One-Off Tradeoff Analysis

Python is the default because it fits the assignment shape with the least ceremony. It has standard `decimal.Decimal`, simple JSON and file-system libraries, lightweight `pytest` and coverage tooling, and direct alignment with the required Python FastMCP server at `mcp/server.py`. It is also faster for iterative agent-generated pipeline runs and screenshots.

Java is viable as a second run because `BigDecimal`, JUnit 5, JaCoCo, and Jackson or equivalent JSON handling can support rigorous money, test, and file-protocol behavior. The cost is more build-system structure, more boilerplate, and a separate Python FastMCP server that reads Java-produced JSON result files because Homework 6 requires `mcp/server.py`.

Decision: generate Python by default and use Java only when the operator explicitly asks for a comparison run or has enough time to carry the additional build-system and documentation overhead.

## Python Profile

Use these concrete defaults for `stack=python`:

| Area | Profile |
|---|---|
| Money | `decimal.Decimal`; never `float` for amounts. |
| JSON | Standard `json` module plus typed dictionaries or dataclasses as useful. |
| Pipeline entry | `integrator.py` with a `main()` function. |
| Agent modules | `agents/transaction_validator.py`, `agents/fraud_detector.py`, and `agents/settlement_processor.py` or `agents/reporting_agent.py`. |
| Common agent function | `process_message(message: dict) -> dict`. |
| Shared protocol | JSON files through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`. |
| Tests | `pytest` with isolated temporary directories. |
| Coverage | `coverage.py` or `pytest-cov`; Athena (Spec Writer)'s temporary ending-context target is 75%. Themis (Test Generator) later owns raising coverage above 80% and adding the blocking coverage hook. |
| Commands | `python integrator.py`, `python -m pytest`, and a non-blocking coverage report command such as `python -m pytest --cov=.` or equivalent. |
| MCP | Python FastMCP server at `mcp/server.py`. |

Specification task cards should name Python files and functions explicitly. Example function names include `load_transactions`, `prepare_shared_directories`, `process_transaction`, `validate_transaction`, `score_fraud_risk`, `settle_transaction`, `write_result`, and `summarize_results`.

## Java Profile

Use these concrete defaults for `stack=java`:

| Area | Profile |
|---|---|
| Money | `BigDecimal`; never `double` or `float` for amounts. |
| Build tool | Maven unless the operator explicitly approves a different Java build plan. |
| JSON | Jackson or equivalent JSON handling. |
| Pipeline entry | `src/main/java/.../Integrator.java` with a `main(String[] args)` method. |
| Agent classes | `TransactionValidator`, `FraudDetector`, and `SettlementProcessor` or `ReportingAgent`. |
| Common agent method | `PipelineMessage processMessage(PipelineMessage message)`. |
| Shared protocol | JSON files through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`. |
| Tests | JUnit 5 with temporary directories. |
| Coverage | JaCoCo; Athena (Spec Writer)'s temporary ending-context target is 75%. Themis (Test Generator) later owns raising coverage above 80% and adding the blocking coverage hook. |
| Commands | `mvn test`, `mvn jacoco:report`, and a concrete pipeline run command such as `mvn exec:java` or a packaged `java -jar` command chosen in the generated spec. |
| MCP | Python FastMCP server at `mcp/server.py` reading the Java pipeline's JSON result files. |

Specification task cards should name Java packages, classes, methods, Maven commands, and test classes explicitly. Do not leave Java output as a language-neutral variant of the Python plan.

## Shared Stack-Invariant Requirements

Both profiles must preserve:

- File-based JSON agent communication through `shared/input`, `shared/processing`, `shared/output`, and `shared/results`.
- A sample-input beginning state from `sample-transactions.json`.
- A final results state where all sample transactions are accounted for.
- Precise decimal money semantics.
- ISO 4217-style currency validation.
- Structured audit logging with timestamp, agent name, transaction ID, and outcome.
- No plaintext PII logging; redact account identifiers in logs and audit examples.
- Hephaestus (Code Generator) Context7 research notes with at least two documented queries.
- Athena (Spec Writer) temporary ending-context coverage target of 75%.
- Themis (Test Generator) later ownership of raising coverage above 80% and adding the blocking coverage hook.
- Product result files and summary shapes that future `pipeline-status` MCP tools can read after `mcp/server.py` exists.
- Athena (Spec Writer) run preservation and final-selection workflow under `docs/agent-runs/` as outer generation evidence, not as a Generated Transaction System Layer requirement.

## Unsupported Values

Reject `stack=auto`, `stack=node`, `stack=go`, `stack=csharp`, and any unlisted stack. A later operator-approved plan amendment may add profiles, but a single generation run must not expand the enum on its own.
