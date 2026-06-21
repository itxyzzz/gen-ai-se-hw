# Validation Checklist

| Check | Result |
|---|---|
| Explicit select-set authorization | Pass; operator requested Hera select latest Python run as canonical |
| Screenshot commit before selection | Pass; committed as `c32277a` |
| `selection-sets.json` valid JSON | Pass; `python -m json.tool docs/agent-runs/selection-sets.json` |
| Agent config check | Pass; TOML parsed and `max_threads=8`, `max_depth=2` |
| Canonical set ID updated | Pass; `python-canonical-20260622-hera-full-set` |
| Java package status | Pass; registered as candidate evidence only |
| Documentation paths | Pass; PR screenshot links are relative to `docs/pr-description-draft.md`; reviewer docs use root canonical paths |
| Stale-path scan | Pass; only source-provenance mention of prior package-set proposal remained in select metadata |
| Privacy scan | Pass; no raw account IDs or secret material found; only reviewer-facing privacy-denial wording matched the scan |
| Tests | Pass; `python -m pytest -p no:cacheprovider` reported 52 passed |
| Coverage gate | Pass; `python scripts/check_coverage_gate.py --stack python --fail-under 80` reported 52 passed and 95.57% total coverage |
| Pipeline smoke | Pass; `python integrator.py` reported `total=8 settled=2 rejected=2 review_required=4 error=0` |

Known limitations from source child runs remain documented: Windows hook-shell limitations and pre-selection MCP helper validation against candidate result paths.
