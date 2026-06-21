# Hephaestus Java Candidate Output Inventory

- Run ID: `20260621-145101-generate-code-java-alternate`
- Parent Hera run ID: `20260621-145059-orchestrate-runs-java-alternate`
- Source Athena run ID: `20260621-145100-write-spec-java-alternate`
- Source spec path: `docs/agent-runs/20260621-145100-write-spec-java-alternate/agent-1-spec/outputs/specification.md`
- Stack: `java`
- Selection status: preserved alternate, not canonical

## Selectable Files

| Candidate path | Intended canonical target if later selected | Kind | SHA-256 |
|---|---|---|---|
| `pom.xml` | `pom.xml` | Maven build | `850B1BA80E5131452157C437BA631ACC06F84CFD750F85492E5C62C7E015D59B` |
| `src/main/java/edu/setu/banking/pipeline/Integrator.java` | `src/main/java/edu/setu/banking/pipeline/Integrator.java` | runtime orchestrator | `6DDD5F86ECE2A3AA9005E0FA3ADC04CEDB7CD388D16024202F2D1ADFD24FC51C` |
| `src/main/java/edu/setu/banking/pipeline/agent/TransactionValidator.java` | `src/main/java/edu/setu/banking/pipeline/agent/TransactionValidator.java` | runtime component | `65C70E6D234ACA65CEB66A4733B0A845BF3821C8B92DB3461838C97CCC2B9578` |
| `src/main/java/edu/setu/banking/pipeline/agent/FraudDetector.java` | `src/main/java/edu/setu/banking/pipeline/agent/FraudDetector.java` | runtime component | `98FC36D7D887DC0CE6793D7AC2FA880B9375564B9A5AF6B895A43D20D495A6C0` |
| `src/main/java/edu/setu/banking/pipeline/agent/SettlementProcessor.java` | `src/main/java/edu/setu/banking/pipeline/agent/SettlementProcessor.java` | runtime component | `8FD43E6BAE88E697869495B8A926D59B5D34942380BABAF68F4D6B291FE1A878` |
| `src/main/java/edu/setu/banking/pipeline/agent/ReportingAgent.java` | `src/main/java/edu/setu/banking/pipeline/agent/ReportingAgent.java` | runtime component | `57CF29D9AE3F46535F807075526D5476AF31521919B539C93E913812FF3EC90F` |
| `src/main/java/edu/setu/banking/pipeline/model/AuditEvent.java` | `src/main/java/edu/setu/banking/pipeline/model/AuditEvent.java` | model | `239AC196FCBC45B6C10F243083A9AEC193D29F8A0ECF6BE78DC43D1849967A4A` |
| `src/main/java/edu/setu/banking/pipeline/model/ProcessingResult.java` | `src/main/java/edu/setu/banking/pipeline/model/ProcessingResult.java` | model | `F75EF833ECE7E587119FB3046D39030D1F1C53B92ED6763CFF59A618B8F8570D` |
| `src/main/java/edu/setu/banking/pipeline/model/Summary.java` | `src/main/java/edu/setu/banking/pipeline/model/Summary.java` | model | `994C6FDF896A08B3922021D655C3ED8A4D39BEDBE97FDB18B152F285A03F75DB` |
| `src/main/java/edu/setu/banking/pipeline/model/TimeSupport.java` | `src/main/java/edu/setu/banking/pipeline/model/TimeSupport.java` | model utility | `A925BEAE628D94716D5228AF3FEAD4A8BD2284AA5AD2909014647CDB49FDD3D8` |
| `src/main/java/edu/setu/banking/pipeline/model/TransactionRecord.java` | `src/main/java/edu/setu/banking/pipeline/model/TransactionRecord.java` | input DTO | `40117C76DDE95AF711EC9CEFBBE4934EB81741EA9F4215231E569CF0A346A942` |
| `src/main/java/edu/setu/banking/pipeline/util/DirectorySupport.java` | `src/main/java/edu/setu/banking/pipeline/util/DirectorySupport.java` | protocol utility | `6E22098CA408CF9B29AE3B3B7EFEF08BDB5E6C3B6137AF64D5F7B9B83185C0F8` |
| `src/main/java/edu/setu/banking/pipeline/util/JsonSupport.java` | `src/main/java/edu/setu/banking/pipeline/util/JsonSupport.java` | JSON utility | `3A440FFE3F19E7DDECC347C87D9C96B50313CD8B26FC6B35402F7CF6A41FBFF5` |
| `src/test/java/edu/setu/banking/pipeline/TransactionValidatorTest.java` | `src/test/java/edu/setu/banking/pipeline/TransactionValidatorTest.java` | baseline test | `235650C14C71860A08496749DAF2BECF02CA3F760A17A5C245465D480DD681E4` |
| `src/test/java/edu/setu/banking/pipeline/FraudDetectorTest.java` | `src/test/java/edu/setu/banking/pipeline/FraudDetectorTest.java` | baseline test | `3105D7FE4B60E8F968786E4C645D369D07496BD82560633D9C3A62BF05105387` |
| `src/test/java/edu/setu/banking/pipeline/SettlementAndReportingTest.java` | `src/test/java/edu/setu/banking/pipeline/SettlementAndReportingTest.java` | baseline test | `53F9A3BC7B878B89ADEEA6E47D32CE9B55D7D3D6BB08AAD9E9789B33392CDF7C` |
| `src/test/java/edu/setu/banking/pipeline/IntegratorPipelineTest.java` | `src/test/java/edu/setu/banking/pipeline/IntegratorPipelineTest.java` | baseline integration test | `5B41E58854A18A7D634A6843F8354B76FE977174BF304DD38EF1AC0383106CAE` |
| `research-notes.md` | `research-notes.md` | Context7 notes | `30C55706ED84F423EF575CF26D12BE083BFF59481D4834945EF2451247A48605` |

## Traceability Files

| Candidate path | Kind | SHA-256 |
|---|---|---|
| `sample-transactions.json` | copied fixture | `771DA836CAAAA42921C628D6CD2E42D52E12687917BA60633E594D526BF4BF12` |

## Excluded Runtime And Tool Outputs

- `shared/`
- `archive/`
- `target/`
- `.mvn/`
- `.coverage*`
- `.pytest_cache/`
- `.test-tmp/`
- `tmp/`
- `__pycache__/`

## Validation Notes

- Baseline `mvn test` passed with 9 tests.
- Baseline pipeline command passed twice and archived prior run-local `shared/`.
- Baseline `jacoco:check` failed at 0.76 before Themis overlay; Themis owns the passing 80 percent gate evidence.
