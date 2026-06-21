"""Run the Homework 6 coverage gate for the selected stack."""

from __future__ import annotations

import argparse
import json
import os
import shutil
import subprocess
import sys
from pathlib import Path


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description=(
            "Run the Homework 6 coverage gate and fail when coverage is below "
            "the threshold."
        )
    )
    parser.add_argument(
        "--stack",
        choices=("auto", "python", "java"),
        default="auto",
        help="Coverage stack to run. Defaults to auto.",
    )
    parser.add_argument(
        "--project-dir",
        type=Path,
        default=Path(__file__).resolve().parents[1],
        help="Project directory to validate. Defaults to the Homework 6 root.",
    )
    parser.add_argument(
        "--fail-under",
        type=int,
        default=80,
        help="Minimum required coverage percentage. Defaults to 80.",
    )
    parser.add_argument(
        "pytest_args",
        nargs=argparse.REMAINDER,
        help="Optional extra arguments passed to pytest after '--'.",
    )
    return parser.parse_args()


def _coverage_ratio(fail_under: int) -> str:
    return f"{fail_under / 100:.2f}"


def _has_jacoco_check_configuration(pom_path: Path) -> bool:
    text = pom_path.read_text(encoding="utf-8")
    return "jacoco" in text.lower() and "<goal>check</goal>" in text.lower()


def _normalized_extra_args(extra_args: list[str]) -> list[str]:
    if extra_args[:1] == ["--"]:
        return extra_args[1:]
    return extra_args


def _resolve_stack(stack: str, project_dir: Path) -> str:
    if stack != "auto":
        return stack
    registry_path = project_dir / "docs" / "agent-runs" / "selection-sets.json"
    if registry_path.exists():
        registry = json.loads(registry_path.read_text(encoding="utf-8"))
        canonical_set_id = registry.get("canonical_set_id")
        selected_set = registry.get("sets", {}).get(canonical_set_id, {})
        selected_stack = selected_set.get("stack")
        if selected_stack in {"python", "java"}:
            return selected_stack
    if (project_dir / "pom.xml").exists():
        return "java"
    return "python"


def run_python_gate(project_dir: Path, fail_under: int, extra_args: list[str] | None = None) -> int:
    extra_args = _normalized_extra_args(extra_args or [])
    temp_path = project_dir / "tmp" / f"coverage-gate-{os.getpid()}"
    shutil.rmtree(temp_path, ignore_errors=True)
    temp_path.mkdir(parents=True, exist_ok=True)

    env = os.environ.copy()
    env["COVERAGE_FILE"] = str(temp_path / ".coverage")

    command = [
        sys.executable,
        "-m",
        "pytest",
        "-p",
        "no:cacheprovider",
        f"--basetemp={temp_path / 'pytest'}",
        "--cov=.",
        f"--cov-fail-under={fail_under}",
        "--cov-report=term-missing",
        *extra_args,
    ]

    print(
        f"Running Homework 6 Python coverage gate from {project_dir} "
        f"with fail-under={fail_under}"
    )
    completed = subprocess.run(command, cwd=project_dir, env=env, check=False)
    shutil.rmtree(temp_path, ignore_errors=True)
    return completed.returncode


def run_java_gate(project_dir: Path, fail_under: int) -> int:
    pom_path = project_dir / "pom.xml"
    if not pom_path.exists():
        print(
            f"Java coverage gate requires pom.xml in project directory: {project_dir}",
            file=sys.stderr,
        )
        return 2

    mvn = shutil.which("mvn") or shutil.which("mvn.cmd")
    if mvn is None:
        print(
            "Java coverage gate requires Maven on PATH to run mvn test "
            "jacoco:report jacoco:check.",
            file=sys.stderr,
        )
        return 2

    if not _has_jacoco_check_configuration(pom_path):
        print(
            "Java coverage gate requires a JaCoCo Maven plugin check goal "
            "configuration in pom.xml.",
            file=sys.stderr,
        )
        return 2

    command = [
        mvn,
        f"-Dcoverage.minimum={_coverage_ratio(fail_under)}",
        "test",
        "jacoco:report",
        "jacoco:check",
    ]
    print(
        f"Running Homework 6 Java coverage gate from {project_dir} "
        f"with fail-under={fail_under}"
    )
    completed = subprocess.run(command, cwd=project_dir, check=False)
    return completed.returncode


def main() -> int:
    args = parse_args()
    project_dir = args.project_dir.resolve()
    stack = _resolve_stack(args.stack, project_dir)
    if stack == "java":
        return run_java_gate(project_dir, args.fail_under)
    return run_python_gate(project_dir, args.fail_under, args.pytest_args)


if __name__ == "__main__":
    raise SystemExit(main())
