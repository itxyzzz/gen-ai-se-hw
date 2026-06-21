import os
import subprocess
import sys
from pathlib import Path

import scripts.check_coverage_gate as coverage_gate


def test_parse_args_accepts_stack_and_project_dir(monkeypatch, tmp_path):
    monkeypatch.setattr(
        sys,
        "argv",
        [
            "check_coverage_gate.py",
            "--stack",
            "java",
            "--project-dir",
            str(tmp_path),
            "--fail-under",
            "85",
        ],
    )

    args = coverage_gate.parse_args()

    assert args.stack == "java"
    assert args.project_dir == tmp_path
    assert args.fail_under == 85


def test_python_mode_uses_project_dir_and_passes_extra_pytest_args(monkeypatch, tmp_path):
    calls = []

    def fake_run(command, cwd, env, check):
        calls.append((command, cwd, env, check))
        return subprocess.CompletedProcess(command, 0)

    monkeypatch.setattr(coverage_gate.subprocess, "run", fake_run)

    exit_code = coverage_gate.run_python_gate(
        tmp_path,
        fail_under=91,
        extra_args=["tests/test_common.py", "-q"],
    )

    command, cwd, env, check = calls[0]
    assert exit_code == 0
    assert cwd == tmp_path
    assert command[:4] == [sys.executable, "-m", "pytest", "-p"]
    assert "--cov=." in command
    assert "--cov-fail-under=91" in command
    assert "tests/test_common.py" in command
    assert "-q" in command
    assert Path(env["COVERAGE_FILE"]).parent.parent == tmp_path / "tmp"
    assert check is False


def test_auto_stack_uses_selection_set_registry(tmp_path):
    registry = tmp_path / "docs" / "agent-runs" / "selection-sets.json"
    registry.parent.mkdir(parents=True)
    registry.write_text(
        '{"canonical_set_id":"java-alt","sets":{"java-alt":{"stack":"java"}}}',
        encoding="utf-8",
    )

    assert coverage_gate._resolve_stack("auto", tmp_path) == "java"


def test_java_mode_fails_clearly_when_pom_is_missing(tmp_path, capsys):
    exit_code = coverage_gate.run_java_gate(tmp_path, fail_under=80)

    captured = capsys.readouterr()
    assert exit_code == 2
    assert "Java coverage gate requires pom.xml" in captured.err
    assert str(tmp_path) in captured.err
