# Domain Rules

- This pipeline is an educational simulation for synthetic transaction-processing workflow design.
- It does not provide legal, banking, AML, sanctions, KYC, PCI, payment-network, or regulatory compliance.
- Input account identifiers, descriptions, and metadata are treated as sensitive even though the fixture is synthetic.
- Reviewer-facing output should use transaction IDs, status values, reason codes, and aggregate counts.
- Supported currency behavior is ISO 4217-style and local to the assignment; `USD`, `EUR`, `GBP`, and `JPY` are sufficient for the Java alternate.
- Risk scoring is deterministic educational logic: high value, very high value, odd-hour timestamp, country signal, and destination-pattern signal.
