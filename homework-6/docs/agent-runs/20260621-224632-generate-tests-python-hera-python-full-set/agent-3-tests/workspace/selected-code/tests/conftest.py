from __future__ import annotations

import json
from pathlib import Path

import pytest


@pytest.fixture
def sample_records() -> list[dict]:
    sample_path = Path(__file__).resolve().parents[1] / "sample-transactions.json"
    return json.loads(sample_path.read_text(encoding="utf-8"))


@pytest.fixture
def sample_input(tmp_path: Path, sample_records: list[dict]) -> Path:
    path = tmp_path / "sample-transactions.json"
    path.write_text(json.dumps(sample_records), encoding="utf-8")
    return path
