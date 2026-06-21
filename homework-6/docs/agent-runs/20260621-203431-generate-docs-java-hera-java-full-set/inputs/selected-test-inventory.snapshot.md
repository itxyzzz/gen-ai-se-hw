# Java Themis Candidate Test Inventory

Run ID: `20260621-201051-generate-tests-java-hera-java-full-set-retry`

Mode: `generate`

Stack: `java`

Target Hephaestus run ID: `20260621-183025-generate-code-java-hera-java-full-set`

Target Hephaestus inventory: `docs/agent-runs/20260621-183025-generate-code-java-hera-java-full-set/agent-2-code/outputs/inventory.md`

Target Hephaestus inventory SHA-256: `3C5B7DB95A30250DEE60A26D17BAD577FA8BFF98A293FA917A1DEA8ECCBCDEB1`

Target Hephaestus package fingerprint: `248ba67b199104cbe06b4b0869cf3143de85ff1dd88da7bf4a13bc85a58ed883`

Source Athena run ID: `20260621-180826-write-spec-java-hera-java-full-set`

Source spec SHA-256: `2FC5B597915CFD59D5786271DB0CDE96B06417DEEC1433D056B76D7023234CFC`

Selectable Themis package fingerprint: `d75aee1b9e76733d78860348fe26608fef17667a88c25e3c7b416e9150b54922`

## Selectable Files

| Kind | Candidate path under `outputs/` | Intended canonical target if selected | SHA-256 | Action |
|---|---|---|---|---|
| Maven test/build configuration | `pom.xml` | `pom.xml` | `C4F5FD42660FB6637D6FECAC27795737C1D204F219EA7100E524BC9917D4ADCD` | Replaces Hephaestus candidate `pom.xml` to keep default 80% coverage and allow `-Dcoverage.minimum` overrides. |
| Unit test | `src/test/java/edu/setu/transactionpipeline/agent/FraudDetectorTest.java` | `src/test/java/edu/setu/transactionpipeline/agent/FraudDetectorTest.java` | `EDBE53542820C8A7D68B1127BD4172E86610ED6F63C5B0C6D80024AEEA7958F7` | Extends baseline selected-code test package. |
| Unit test | `src/test/java/edu/setu/transactionpipeline/agent/SettlementProcessorTest.java` | `src/test/java/edu/setu/transactionpipeline/agent/SettlementProcessorTest.java` | `72D082129511AB12469545753C2A19D678336328021C5EF241827F80851EB24A` | Extends baseline selected-code test package. |
| Unit test | `src/test/java/edu/setu/transactionpipeline/agent/TransactionValidatorTest.java` | `src/test/java/edu/setu/transactionpipeline/agent/TransactionValidatorTest.java` | `004FFFB759ECD6A972C16266ED63C78BE21FFDB4B299488C52171E2814B598FD` | Extends baseline selected-code test package. |
| Build contract test | `src/test/java/edu/setu/transactionpipeline/BuildContractTest.java` | `src/test/java/edu/setu/transactionpipeline/BuildContractTest.java` | `84AE2284FBA79029C0A5E3BE4F1A23646930652CDA003DB6394BFB40AEF3EA47` | Replaces baseline test to verify default 80% and override-aware JaCoCo wiring. |
| CLI test | `src/test/java/edu/setu/transactionpipeline/cli/ValidateTransactionsCommandTest.java` | `src/test/java/edu/setu/transactionpipeline/cli/ValidateTransactionsCommandTest.java` | `64598A0AEED73A55B816C944E4C779D5BA7DF361DF2A247AB34340F0895FEF72` | Extends baseline selected-code test package. |
| Integration test | `src/test/java/edu/setu/transactionpipeline/IntegrationPipelineTest.java` | `src/test/java/edu/setu/transactionpipeline/IntegrationPipelineTest.java` | `C3A3B54D3CB4F0ED83BB710B83FCD7887A3F02E8AB108029E9CF0250CCD5CC66` | Extends baseline selected-code test package. |
| Schema test | `src/test/java/edu/setu/transactionpipeline/io/JsonAndResultSchemaTest.java` | `src/test/java/edu/setu/transactionpipeline/io/JsonAndResultSchemaTest.java` | `5B9B20FC68D6B82814E0AC0B67433D96AEE7A0C246CE858AACAA8A3984AFED02` | Extends baseline selected-code test package. |
| Filesystem isolation test | `src/test/java/edu/setu/transactionpipeline/io/SharedDirectoryManagerTest.java` | `src/test/java/edu/setu/transactionpipeline/io/SharedDirectoryManagerTest.java` | `86FC9D0719C35D72F9E176F80678D7967CB8B9F9DD0CDE58409003AC7AA16634` | Extends baseline selected-code test package. |
| Privacy test | `src/test/java/edu/setu/transactionpipeline/privacy/PrivacyGuardTest.java` | `src/test/java/edu/setu/transactionpipeline/privacy/PrivacyGuardTest.java` | `00C5EAC82A73FA15BA04FB3971D47C22E1A8F872C7A1DA5F9DBE721A429BE3C2` | Extends baseline selected-code test package. |
| Test fixture helper | `src/test/java/edu/setu/transactionpipeline/TestFixtures.java` | `src/test/java/edu/setu/transactionpipeline/TestFixtures.java` | `1CD0AD114E75AF00515C0D2CFE9CA75FD05D71E6DE159DD7DB949644276215DC` | Extends baseline selected-code test package. |
| Themis quality expansion | `src/test/java/edu/setu/transactionpipeline/ThemisQualityTest.java` | `src/test/java/edu/setu/transactionpipeline/ThemisQualityTest.java` | `48FC12CA0D18689072D1ABC0B27C22E9E16D1F0D839DA565C7A953548898A716` | Creates focused test coverage for validation-only isolation, safe run evidence, archive evidence, and coverage gate configuration. |

## Explicitly Excluded From Selection

The following paths are not selectable and must not be copied to canonical targets:

- `agent-3-tests/workspace/`
- `agent-3-tests/evidence/`
- `shared/`
- `archive/`
- `target/`
- `target/surefire-reports/`
- `target/failsafe-reports/`
- `target/site/jacoco/`
- `.coverage*`
- `.pytest_cache/`
- `.test-tmp/`
- `tmp/`
- caches
- `__pycache__/`
- runtime outputs such as `validation-report.json`
- copied validation support files such as `scripts/check_coverage_gate.py`
- Maven local repositories, `.class`, `.jar`, JaCoCo execution data, and temporary files
