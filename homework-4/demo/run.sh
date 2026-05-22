#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."
npm run verify:baseline
npm run pipeline:mock -- --scenario bug-001 --run run-001
npm run promote -- --scenario bug-001 --run run-001
npm test
npm run compare -- --scenario bug-001
