# Themis Java Candidate Test Inventory

Status: BLOCKED and not selectable.

Run ID: `20260621-191017-generate-tests-java-hera-java-full-set`

Target Hephaestus run ID: `20260621-183025-generate-code-java-hera-java-full-set`

Target Hephaestus inventory: `docs/agent-runs/20260621-183025-generate-code-java-hera-java-full-set/agent-2-code/outputs/inventory.md`

Target Hephaestus inventory SHA-256: `3C5B7DB95A30250DEE60A26D17BAD577FA8BFF98A293FA917A1DEA8ECCBCDEB1`

Target Hephaestus package fingerprint: `248ba67b199104cbe06b4b0869cf3143de85ff1dd88da7bf4a13bc85a58ed883`

Source Athena run ID: `20260621-180826-write-spec-java-hera-java-full-set`

Source spec SHA-256: `2FC5B597915CFD59D5786271DB0CDE96B06417DEEC1433D056B76D7023234CFC`

## Inventory Completeness

This inventory is intentionally blocked. It records current output artifacts only so Hera and Clio do not mistake the run for a validated selectable package.

The run was interrupted before:

- Themis-owned expansion tests were authored.
- Candidate output SHA-256 fingerprints were recomputed.
- `project-under-test/` was rebuilt and validated.
- Maven/JUnit/JaCoCo checks were run.
- Package-level selected test fingerprint was computed.

## Current Output Artifacts

The following baseline test files were copied from the targeted Hephaestus package into `agent-3-tests/outputs/src/test/java/`. Their hashes are expected to match the source Hephaestus inventory because no edits were made before interruption, but this run did not recompute them after copy.

| Candidate path under outputs | Intended canonical target if selected | Kind | SHA-256 status | Selection action |
|---|---|---|---|---|
| `src/test/java/edu/setu/transactionpipeline/BuildContractTest.java` | `src/test/java/edu/setu/transactionpipeline/BuildContractTest.java` | Java baseline test | not recomputed in blocked run; source inventory hash `DB394A4AC19C1D4A5F1170F9E28924A251769B4FCC7C979C8592F2C1CD671472` | blocked |
| `src/test/java/edu/setu/transactionpipeline/IntegrationPipelineTest.java` | `src/test/java/edu/setu/transactionpipeline/IntegrationPipelineTest.java` | Java baseline integration test | not recomputed in blocked run; source inventory hash `C3A3B54D3CB4F0ED83BB710B83FCD7887A3F02E8AB108029E9CF0250CCD5CC66` | blocked |
| `src/test/java/edu/setu/transactionpipeline/TestFixtures.java` | `src/test/java/edu/setu/transactionpipeline/TestFixtures.java` | Java baseline test fixture helper | not recomputed in blocked run; source inventory hash `1CD0AD114E75AF00515C0D2CFE9CA75FD05D71E6DE159DD7DB949644276215DC` | blocked |
| `src/test/java/edu/setu/transactionpipeline/agent/FraudDetectorTest.java` | `src/test/java/edu/setu/transactionpipeline/agent/FraudDetectorTest.java` | Java baseline component test | not recomputed in blocked run; source inventory hash `EDBE53542820C8A7D68B1127BD4172E86610ED6F63C5B0C6D80024AEEA7958F7` | blocked |
| `src/test/java/edu/setu/transactionpipeline/agent/SettlementProcessorTest.java` | `src/test/java/edu/setu/transactionpipeline/agent/SettlementProcessorTest.java` | Java baseline component test | not recomputed in blocked run; source inventory hash `72D082129511AB12469545753C2A19D678336328021C5EF241827F80851EB24A` | blocked |
| `src/test/java/edu/setu/transactionpipeline/agent/TransactionValidatorTest.java` | `src/test/java/edu/setu/transactionpipeline/agent/TransactionValidatorTest.java` | Java baseline component test | not recomputed in blocked run; source inventory hash `004FFFB759ECD6A972C16266ED63C78BE21FFDB4B299488C52171E2814B598FD` | blocked |
| `src/test/java/edu/setu/transactionpipeline/cli/ValidateTransactionsCommandTest.java` | `src/test/java/edu/setu/transactionpipeline/cli/ValidateTransactionsCommandTest.java` | Java baseline CLI test | not recomputed in blocked run; source inventory hash `64598A0AEED73A55B816C944E4C779D5BA7DF361DF2A247AB34340F0895FEF72` | blocked |
| `src/test/java/edu/setu/transactionpipeline/io/JsonAndResultSchemaTest.java` | `src/test/java/edu/setu/transactionpipeline/io/JsonAndResultSchemaTest.java` | Java baseline schema test | not recomputed in blocked run; source inventory hash `5B9B20FC68D6B82814E0AC0B67433D96AEE7A0C246CE858AACAA8A3984AFED02` | blocked |
| `src/test/java/edu/setu/transactionpipeline/io/SharedDirectoryManagerTest.java` | `src/test/java/edu/setu/transactionpipeline/io/SharedDirectoryManagerTest.java` | Java baseline IO test | not recomputed in blocked run; source inventory hash `86FC9D0719C35D72F9E176F80678D7967CB8B9F9DD0CDE58409003AC7AA16634` | blocked |
| `src/test/java/edu/setu/transactionpipeline/privacy/PrivacyGuardTest.java` | `src/test/java/edu/setu/transactionpipeline/privacy/PrivacyGuardTest.java` | Java baseline privacy test | not recomputed in blocked run; source inventory hash `00C5EAC82A73FA15BA04FB3971D47C22E1A8F872C7A1DA5F9DBE721A429BE3C2` | blocked |

## Accidental Duplicate Output

An accidental duplicate baseline tree exists under:

`agent-3-tests/outputs/src/java/`

It is not selectable. Cleanup was attempted after verifying it was inside the run-local outputs folder, but Windows returned `Access to the path is denied`; the parent interrupted before a retry or alternative cleanup could complete.

## Explicitly Excluded From Selection

The following paths are excluded from selectable inventory:

- `agent-3-tests/workspace/`
- `agent-3-tests/evidence/`
- `shared/`
- `archive/`
- `target/`
- `.coverage*`
- `.pytest_cache/`
- `.test-tmp/`
- `tmp/`
- `__pycache__/`
- `.mvn/`
- Maven local caches
- Maven Surefire/Failsafe outputs
- JaCoCo outputs
- `.class`
- `.jar`
- `validation-report.json`
- `agent-3-tests/outputs/src/java/` accidental duplicate tree

## Selection Warning

Do not select or canonical-copy this blocked package. A resumed Themis run should first remove or ignore the accidental duplicate, write Themis-owned expansion tests under `outputs/src/test/java/`, rebuild `workspace/project-under-test/`, run validation from that workspace, then regenerate this inventory with real SHA-256 fingerprints and a selected package fingerprint.
