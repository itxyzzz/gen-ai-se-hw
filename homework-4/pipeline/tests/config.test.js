import assert from "node:assert/strict";
import test from "node:test";
import { loadAgentSpecs, validateManifestAgents } from "../lib/agentSpec.js";
import { loadConfig } from "../lib/config.js";

test("loads pipeline config and validates agent specs", async () => {
  const config = await loadConfig();
  const specs = await loadAgentSpecs();

  await validateManifestAgents(config, specs);

  assert.equal(config.defaultScenario, "bug-001");
  assert.deepEqual(config.stageOrder, [
    "bug-researcher",
    "research-verifier",
    "bug-planner",
    "bug-fixer",
    "security-verifier",
    "unit-test-generator"
  ]);
  assert.equal(specs.get("research-verifier").frontmatter.skills[0], "skills/research-quality-measurement.md");
  assert.equal(specs.get("unit-test-generator").frontmatter.skills[0], "skills/unit-tests-FIRST.md");
});
