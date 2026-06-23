#!/usr/bin/env bash
#
# demo.sh — zero-manual-steps demo of the flexible transaction pipeline.
#
# Shows the full chain end to end:
#   1. transaction generator (upstream support tool) produces sample input
#   2. orchestrator runs it through the configured stages (config/pipeline.json)
#   3. orchestrator re-runs it with a *different* stage list (flexibility)
#   4. results + run summary are displayed
#
# Stdlib only — no FastMCP / network / manual setup required. Run from anywhere:
#   ./demo.sh
#
set -euo pipefail

# --- locate the project (this script's directory) ---------------------------
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# --- pick a python interpreter ----------------------------------------------
if command -v python3 >/dev/null 2>&1; then
  PY=python3
elif command -v python >/dev/null 2>&1; then
  PY=python
else
  echo "ERROR: python3 not found on PATH." >&2
  exit 1
fi

# --- pretty-print JSON (jq if available, else stdlib) -----------------------
pp() {
  if command -v jq >/dev/null 2>&1; then
    jq . "$1"
  else
    "$PY" -m json.tool "$1"
  fi
}

banner() {
  echo
  echo "============================================================"
  echo "  $1"
  echo "============================================================"
}

GENERATED="demo-input.json"
SUMMARY="shared/results/summary.json"

# --- 0. show the configurable pipeline --------------------------------------
banner "0. Configured pipeline stages (config/pipeline.json)"
"$PY" integrator.py --list-stages

# --- 1. generate transactions (seeded → reproducible) -----------------------
banner "1. Generate transactions  →  $GENERATED"
"$PY" scripts/generate_transactions.py --count 8 --seed 42 --output "$GENERATED"
echo "Generated $("$PY" -c "import json,sys;print(len(json.load(open('$GENERATED'))))") transactions."

# --- 2. run the default (configured) pipeline -------------------------------
banner "2. Run pipeline with the configured stages"
"$PY" integrator.py --input "$GENERATED"
echo
echo "--- summary.json ---"
pp "$SUMMARY"

# --- 3. run the SAME input with a DIFFERENT stage list (flexibility) --------
banner "3. Re-run with changed stages: transaction_validator,settlement_processor"
echo "(fraud_detector removed → no transactions get flagged for review)"
"$PY" integrator.py --input "$GENERATED" --stages transaction_validator,settlement_processor
echo
echo "--- summary.json (note review_required is now 0) ---"
pp "$SUMMARY"

# --- 4. show a couple of per-transaction results ----------------------------
banner "4. Sample per-transaction results (privacy-safe)"
for f in shared/results/TXN001.json shared/results/TXN002.json; do
  if [ -f "$f" ]; then
    echo "--- $f ---"
    pp "$f"
    echo
  fi
done

banner "Demo complete"
echo "Full results in: shared/results/   (per-transaction + summary.json + pipeline-status.json)"
