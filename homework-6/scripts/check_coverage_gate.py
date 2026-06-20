"""Run the Homework 6 coverage gate from the repository homework root."""

from __future__ import annotations

import argparse
import os
import shutil
import subprocess
import sys
from pathlib import Path


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Run pytest coverage and fail when coverage is below the threshold."
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


def main() -> int:
    args = parse_args()
    homework_root = Path(__file__).resolve().parents[1]
    extra_args = args.pytest_args
    if extra_args[:1] == ["--"]:
        extra_args = extra_args[1:]

    temp_path = homework_root / "tmp" / f"coverage-gate-{os.getpid()}"
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
        f"--cov-fail-under={args.fail_under}",
        "--cov-report=term-missing",
        *extra_args,
    ]

    print(
        f"Running Homework 6 coverage gate from {homework_root} "
        f"with fail-under={args.fail_under}"
    )
    completed = subprocess.run(command, cwd=homework_root, env=env, check=False)
    shutil.rmtree(temp_path, ignore_errors=True)
    return completed.returncode


if __name__ == "__main__":
    raise SystemExit(main())
