# Selected Code Inventory Snapshot

Status: BLOCKED snapshot.

The full Hephaestus inventory was read from:

`docs/agent-runs/20260621-183025-generate-code-java-hera-java-full-set/agent-2-code/outputs/inventory.md`

Inventory SHA-256:

`3C5B7DB95A30250DEE60A26D17BAD577FA8BFF98A293FA917A1DEA8ECCBCDEB1`

Package fingerprint:

`248ba67b199104cbe06b4b0869cf3143de85ff1dd88da7bf4a13bc85a58ed883`

Source package was copied to:

`docs/agent-runs/20260621-191017-generate-tests-java-hera-java-full-set/agent-3-tests/workspace/selected-code/`

The copy completed before interruption. No verification hash pass over the copied workspace was completed after copying.

Important inventory facts observed:

- The Java package is a preserved candidate only.
- Python canonical package set remains protected.
- Java package includes Maven build, runtime source, CLI support, IO/model/privacy support, baseline JUnit tests, and `sample-transactions.json`.
- Runtime/tool outputs explicitly excluded by Hephaestus include `shared/`, `archive/`, `target/`, `validation-report.json`, `.mvn/`, local Maven caches, temporary Maven settings, IDE/OS files, `.class`, `.jar`, coverage, surefire, failsafe, and temporary files.

The interrupted Themis run did not complete a new selected-code fingerprint or compare the copied workspace against the source inventory.
