import { loadAgentSpecs, validateManifestAgents } from "./lib/agentSpec.js";
import { loadConfig } from "./lib/config.js";
import { runMockPipeline } from "../adapters/mock.js";
import { runOpenAiSdkPipeline } from "../adapters/openai-sdk.js";
import { prepareCodexChat, validateCodexChat } from "../adapters/codex-chat.js";

const args = parseArgs(process.argv.slice(2));
const config = await loadConfig();
const specs = await loadAgentSpecs();
await validateManifestAgents(config, specs);

const adapter = args.adapter ?? config.defaultAdapter;
const options = {
  scenarioId: args.scenario ?? config.defaultScenario,
  runId: args.run ?? config.defaultRunId,
  model: args.model,
  reasoning: args.reasoning
};

if (adapter === "mock") {
  const { workspace, metadata } = await runMockPipeline(config, options);
  process.stdout.write(`Mock pipeline ${metadata.status}: ${workspace.runRoot}\n`);
} else if (adapter === "openai-sdk") {
  const { workspace, metadata } = await runOpenAiSdkPipeline(config, options);
  process.stdout.write(`OpenAI SDK pipeline ${metadata.status}: ${workspace.runRoot}\n`);
  if (metadata.status === "blocked") {
    process.stdout.write(`${metadata.blocker}\n`);
  }
} else if (adapter === "codex-chat" && args.prepare) {
  const { workspace, promptRoot } = await prepareCodexChat(config, options);
  process.stdout.write(`Prepared Codex chat prompts for ${workspace.runRoot}: ${promptRoot}\n`);
} else if (adapter === "codex-chat" && args.validate) {
  const { runRoot, metadata } = await validateCodexChat(config, options);
  process.stdout.write(`Validated Codex chat artifacts for ${runRoot}: ${metadata.status}\n`);
} else {
  throw new Error(`Unsupported adapter mode: ${adapter}`);
}

function parseArgs(argv) {
  const parsed = {};
  for (let index = 0; index < argv.length; index += 1) {
    const current = argv[index];
    if (current === "--adapter") {
      parsed.adapter = argv[index + 1];
      index += 1;
    } else if (current === "--scenario") {
      parsed.scenario = argv[index + 1];
      index += 1;
    } else if (current === "--run") {
      parsed.run = argv[index + 1];
      index += 1;
    } else if (current === "--model") {
      parsed.model = argv[index + 1];
      index += 1;
    } else if (current === "--reasoning") {
      parsed.reasoning = argv[index + 1];
      index += 1;
    } else if (current === "--prepare") {
      parsed.prepare = true;
    } else if (current === "--validate") {
      parsed.validate = true;
    }
  }
  return parsed;
}
