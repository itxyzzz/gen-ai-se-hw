const expectedFailures = [
  "line total multiplication",
  "SAVE10 percentage discount",
  "catalog traversal rejection"
];
import { resolveHomeworkPath } from "./lib/config.js";
import { validateSeededBaseline } from "./lib/appValidation.js";

const failures = await validateSeededBaseline(resolveHomeworkPath("app/baseline"));
const allExpectedFailuresPresent = expectedFailures.every((failureName) => failures.includes(failureName));

if (allExpectedFailuresPresent) {
  process.stdout.write(`Baseline verification passed: expected seeded failures were reproduced (${failures.join(", ")}).\n`);
  process.exit(0);
}

process.stderr.write("Baseline verification failed: expected seeded failures were not reproduced.\n");
process.stderr.write(`Observed failures: ${failures.join(", ")}\n`);
process.exit(1);
