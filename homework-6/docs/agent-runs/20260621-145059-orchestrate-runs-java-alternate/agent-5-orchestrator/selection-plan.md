# Selection Plan

No selection is authorized by this Hera `generate-set` run.

## Current Recommendation

Preserve the Java alternate for later `compare-set` against `python-canonical-20260621`.

## Required Before Any Future Selection

1. Run Hera `compare-set` for the named Python canonical package and Java alternate runs.
2. Decide whether Java should remain an alternate, be registered as a candidate set, or replace canonical Python.
3. If selection is requested, use Hera `select-set` with an explicit operator instruction naming the target package set or run.
4. Verify every inventory-declared canonical copy target before copying.
5. Update `docs/agent-runs/selection-sets.json` and `docs/agent-runs/final-selection.md` only during explicit selection.

This file is not authorization to copy canonical files.
