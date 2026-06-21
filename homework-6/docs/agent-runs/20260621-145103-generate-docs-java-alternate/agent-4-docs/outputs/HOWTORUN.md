# How To Run The Java Alternate

Run these commands from:

```powershell
docs\agent-runs\20260621-145101-generate-code-java-alternate\agent-2-code\outputs
```

## 1. Test The Baseline Java Package

```powershell
mvn -s ..\validation-settings.xml -gs ..\validation-settings.xml test
```

Expected result: 9 baseline JUnit tests pass.

## 2. Run The Java Pipeline

```powershell
mvn -s ..\validation-settings.xml -gs ..\validation-settings.xml exec:java '-Dexec.args=--input sample-transactions.json --shared-dir shared'
```

Expected safe summary:

```text
Processed 8 transactions: settled=2 rejected=2 review_required=4 error=0
```

## 3. Validate The Themis Overlay

Run from:

```powershell
docs\agent-runs\20260621-145102-generate-tests-java-alternate\agent-3-tests\workspace\project-under-test
```

```powershell
mvn -s ..\..\..\..\20260621-145101-generate-code-java-alternate\agent-2-code\validation-settings.xml -gs ..\..\..\..\20260621-145101-generate-code-java-alternate\agent-2-code\validation-settings.xml test jacoco:report jacoco:check
```

Expected result:

- 21 tests pass.
- JaCoCo reports that all coverage checks have been met.

## 4. Known Environment Note

This machine has a Maven mirror pointing at an unavailable host. The run-local `validation-settings.xml` override is used so preserved Java validation can resolve from Maven Central. The repository Python coverage helper does not currently pass that override to Maven, so its Java mode is recorded as blocked for this alternate.
