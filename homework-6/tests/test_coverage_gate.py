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


def test_parse_args_accepts_java_maven_settings(monkeypatch, tmp_path):
    user_settings = tmp_path / "settings.xml"
    global_settings = tmp_path / "global-settings.xml"
    monkeypatch.setattr(
        sys,
        "argv",
        [
            "check_coverage_gate.py",
            "--stack",
            "java",
            "--project-dir",
            str(tmp_path),
            "--maven-settings",
            str(user_settings),
            "--maven-global-settings",
            str(global_settings),
        ],
    )

    args = coverage_gate.parse_args()

    assert args.maven_settings == user_settings
    assert args.maven_global_settings == global_settings


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


def _write_jacoco_pom(project_dir: Path) -> None:
    (project_dir / "pom.xml").write_text(
        """
<project>
  <build>
    <plugins>
      <plugin>
        <artifactId>jacoco-maven-plugin</artifactId>
        <executions>
          <execution>
            <goals>
              <goal>check</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
</project>
""".strip(),
        encoding="utf-8",
    )


def test_java_mode_default_command_is_unchanged(monkeypatch, tmp_path):
    _write_jacoco_pom(tmp_path)
    calls = []

    def fake_run(command, cwd, check):
        calls.append((command, cwd, check))
        return subprocess.CompletedProcess(command, 0)

    monkeypatch.setattr(coverage_gate.shutil, "which", lambda name: "mvn.cmd")
    monkeypatch.setattr(coverage_gate.subprocess, "run", fake_run)

    exit_code = coverage_gate.run_java_gate(tmp_path, fail_under=80)

    command, cwd, check = calls[0]
    assert exit_code == 0
    assert cwd == tmp_path
    assert command == [
        "mvn.cmd",
        "-Dcoverage.minimum=0.80",
        "test",
        "jacoco:report",
        "jacoco:check",
    ]
    assert check is False


def test_java_mode_includes_maven_settings_overrides(monkeypatch, tmp_path):
    _write_jacoco_pom(tmp_path)
    user_settings = tmp_path / "settings.xml"
    global_settings = tmp_path / "global-settings.xml"
    user_settings.write_text("<settings/>", encoding="utf-8")
    global_settings.write_text("<settings/>", encoding="utf-8")
    calls = []

    def fake_run(command, cwd, check):
        calls.append((command, cwd, check))
        return subprocess.CompletedProcess(command, 0)

    monkeypatch.setattr(coverage_gate.shutil, "which", lambda name: "mvn.cmd")
    monkeypatch.setattr(coverage_gate.subprocess, "run", fake_run)

    exit_code = coverage_gate.run_java_gate(
        tmp_path,
        fail_under=80,
        maven_settings=Path("settings.xml"),
        maven_global_settings=Path("global-settings.xml"),
    )

    command, cwd, check = calls[0]
    assert exit_code == 0
    assert cwd == tmp_path
    assert command == [
        "mvn.cmd",
        "-s",
        str(user_settings.resolve()),
        "-gs",
        str(global_settings.resolve()),
        "-Dcoverage.minimum=0.80",
        "test",
        "jacoco:report",
        "jacoco:check",
    ]
    assert check is False
