import { loadConfig } from "./lib/config.js";
import { copyRunToCurrent, markPromoted } from "./lib/workspace.js";

const args = parseArgs(process.argv.slice(2));
const config = await loadConfig();
const scenarioId = args.scenario ?? config.defaultScenario;
const runId = args.run ?? config.defaultRunId;

const targetApp = await copyRunToCurrent(config, scenarioId, runId);
await markPromoted(scenarioId, runId);

process.stdout.write(`Promoted ${scenarioId}/${runId} to ${targetApp}\n`);

function parseArgs(argv) {
  const parsed = {};
  for (let index = 0; index < argv.length; index += 1) {
    const current = argv[index];
    if (current === "--scenario") {
      parsed.scenario = argv[index + 1];
      index += 1;
    } else if (current === "--run") {
      parsed.run = argv[index + 1];
      index += 1;
    }
  }
  return parsed;
}
