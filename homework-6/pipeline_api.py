"""Local REST-shaped runtime API for pipeline message persistence."""

from __future__ import annotations

from copy import deepcopy
from dataclasses import dataclass
from collections import Counter
from pathlib import Path
import shutil
from typing import Any
from uuid import uuid4

from agents import reporting_agent
from agents.common import (
    COMPONENT_INTEGRATOR,
    COMPONENT_VALIDATOR,
    GENERATED_CODE_RUN_ID,
    PIPELINE_VERSION,
    REASON_PROCESSING_ERROR,
    SOURCE_SPEC_RUN_ID,
    SOURCE_SPEC_SHA256,
    STATUS_ERROR,
    STATUS_REJECTED,
    STATUS_REVIEW_REQUIRED,
    STATUS_SETTLED,
    COMPONENT_REPORTING,
    append_audit_event,
    append_component_history,
    create_message,
    get_protocol_paths,
    safe_json_dump,
    safe_json_load,
    utc_now,
)


PROTOCOL_FILE_STAGES = {"processing", "output"}
MEMORY_ONLY_STAGES = {"validated", "scored"}
RESULT_STAGE = "result"
ALLOWED_STAGES = PROTOCOL_FILE_STAGES | MEMORY_ONLY_STAGES | {RESULT_STAGE}


@dataclass
class PipelineRunState:
    run_id: str
    paths: dict[str, Path]
    expected_transaction_ids: list[str]
    messages: dict[str, dict[str, Any]]


class PipelineRuntimeApi:
    """In-process REST-shaped API that owns runtime file persistence."""

    def __init__(self) -> None:
        self._runs: dict[str, PipelineRunState] = {}

    def create_run(
        self,
        base_dir: Path = Path("."),
        shared_dir_name: str = "shared",
        input_records: list[dict[str, Any]] | None = None,
        runtime_run_id: str | None = None,
        generated_at: str | None = None,
    ) -> dict[str, Any]:
        records = input_records or []
        run_id = runtime_run_id or str(uuid4())
        paths = self._prepare_shared_directories(Path(base_dir), shared_dir_name)
        self._write_run_provenance(paths, run_id, generated_at or utc_now())
        expected_ids: list[str] = []
        messages: dict[str, dict[str, Any]] = {}

        for index, transaction in enumerate(records, start=1):
            transaction_id = str(transaction.get("transaction_id") or f"UNKNOWN-{index:03d}")
            expected_ids.append(transaction_id)
            message = create_message(
                source_agent=COMPONENT_INTEGRATOR,
                target_agent=COMPONENT_VALIDATOR,
                data=self._safe_input_transaction(transaction, transaction_id),
            )
            messages[transaction_id] = message
            safe_json_dump(message, paths["input"] / f"{transaction_id}.json")

        self._runs[run_id] = PipelineRunState(
            run_id=run_id,
            paths=paths,
            expected_transaction_ids=expected_ids,
            messages=messages,
        )
        return {
            "run_id": run_id,
            "expected_transaction_ids": list(expected_ids),
            "message_count": len(messages),
            "paths": {
                "shared": str(paths["shared"]),
                "input": str(paths["input"]),
                "processing": str(paths["processing"]),
                "output": str(paths["output"]),
                "results": str(paths["results"]),
            },
        }

    def get_message(self, run_id: str, transaction_id: str) -> dict[str, Any]:
        state = self._state_for(run_id)
        self._require_transaction(state, transaction_id)
        return {
            "run_id": run_id,
            "transaction_id": transaction_id,
            "message": deepcopy(state.messages[transaction_id]),
        }

    def record_stage(self, run_id: str, transaction_id: str, stage: str, message: dict[str, Any]) -> dict[str, Any]:
        state = self._state_for(run_id)
        self._require_transaction(state, transaction_id)
        if stage not in ALLOWED_STAGES:
            raise ValueError(f"Unknown stage: {stage}")

        safe_message = deepcopy(message)
        reporting_agent.assert_privacy_safe(safe_message)
        if stage == RESULT_STAGE:
            append_component_history(safe_message, COMPONENT_REPORTING)
            append_audit_event(
                safe_message,
                COMPONENT_REPORTING,
                safe_message.get("data", {}).get("status", STATUS_ERROR),
            )
        state.messages[transaction_id] = safe_message
        if stage in PROTOCOL_FILE_STAGES:
            safe_json_dump(safe_message, state.paths[stage] / f"{transaction_id}.json")
        if stage == RESULT_STAGE:
            payload = reporting_agent.build_transaction_status_payload(safe_message)
            safe_json_dump(payload, state.paths["results"] / f"{transaction_id}.json")
        return {
            "run_id": run_id,
            "transaction_id": transaction_id,
            "stage": stage,
            "status": "recorded",
        }

    def record_error(
        self,
        run_id: str,
        transaction_id: str,
        reason_code: str = REASON_PROCESSING_ERROR,
    ) -> dict[str, Any]:
        state = self._state_for(run_id)
        self._require_transaction(state, transaction_id)
        safe_reason = reason_code or REASON_PROCESSING_ERROR
        payload = reporting_agent.build_error_result_payload(transaction_id, safe_reason)
        safe_json_dump(payload, state.paths["results"] / f"{payload['transaction_id']}.json")
        state.messages[transaction_id] = {
            "data": {
                "transaction_id": transaction_id,
                "status": STATUS_ERROR,
                "reason_codes": [safe_reason],
            }
        }
        return {
            "run_id": run_id,
            "transaction_id": transaction_id,
            "status": STATUS_ERROR,
            "reason_codes": [safe_reason],
        }

    def finalize_run(self, run_id: str) -> dict[str, Any]:
        state = self._state_for(run_id)
        summary = self._build_results_summary(state)
        safe_json_dump(summary, state.paths["results"] / "summary.json")
        pipeline_status = reporting_agent.build_pipeline_status(summary)
        safe_json_dump(pipeline_status, state.paths["results"] / "pipeline-status.json")
        return summary

    def _state_for(self, run_id: str) -> PipelineRunState:
        try:
            return self._runs[run_id]
        except KeyError:
            raise ValueError(f"Unknown run_id: {run_id}") from None

    @staticmethod
    def _require_transaction(state: PipelineRunState, transaction_id: str) -> None:
        if transaction_id not in state.messages:
            raise ValueError(f"Unknown transaction_id: {transaction_id}")

    @staticmethod
    def _prepare_shared_directories(base_dir: Path, shared_dir_name: str) -> dict[str, Path]:
        paths = get_protocol_paths(base_dir, shared_dir_name)
        shared = paths["shared"]
        if shared.exists():
            archive_root = paths["archive"]
            archive_root.mkdir(parents=True, exist_ok=True)
            index = 1
            while True:
                destination = archive_root / f"{shared.name}-{index:03d}"
                if not destination.exists():
                    shutil.copytree(shared, destination)
                    break
                index += 1
        for key in ("input", "processing", "output", "results"):
            paths[key].mkdir(parents=True, exist_ok=True)
        return paths

    @staticmethod
    def _write_run_provenance(paths: dict[str, Path], run_id: str, generated_at: str) -> Path:
        payload = {
            "schema_version": 1,
            "runtime_run_id": run_id,
            "generated_at": generated_at,
            "stack": "python",
            "source_spec_run_id": SOURCE_SPEC_RUN_ID,
            "source_spec_path": "docs/agent-runs/20260621-220037-write-spec-python-hera-python-full-set/agent-1-spec/outputs/specification.md",
            "source_spec_sha256": SOURCE_SPEC_SHA256,
            "generated_code_run_id": GENERATED_CODE_RUN_ID,
            "pipeline_version": PIPELINE_VERSION,
            "output_inventory_path": "docs/agent-runs/20260621-222543-generate-code-python-hera-python-full-set/agent-2-code/outputs/inventory.md",
        }
        return safe_json_dump(payload, paths["run_provenance"])

    @staticmethod
    def _build_results_summary(state: PipelineRunState) -> dict[str, Any]:
        results_dir = state.paths["results"]
        result_files = sorted(path.name for path in results_dir.glob("TXN*.json"))
        records = [safe_json_load(results_dir / name) for name in result_files]
        status_counts = Counter(str(item.get("status", STATUS_ERROR)) for item in records)
        reason_counts = Counter(code for item in records for code in item.get("reason_codes", []))
        observed_ids = sorted(str(item.get("transaction_id", "")) for item in records)
        expected_ids = sorted(state.expected_transaction_ids)
        summary = {
            "schema_version": 1,
            "runtime_run_id": state.run_id,
            "generated_at": utc_now(),
            "total_records": len(records),
            "settled": status_counts.get(STATUS_SETTLED, 0),
            "rejected": status_counts.get(STATUS_REJECTED, 0),
            "review_required": status_counts.get(STATUS_REVIEW_REQUIRED, 0),
            "error": status_counts.get(STATUS_ERROR, 0),
            "status_counts": dict(sorted(status_counts.items())),
            "reason_code_counts": dict(sorted(reason_counts.items())),
            "expected_transaction_ids": expected_ids,
            "result_files": result_files,
            "privacy_check": "passed",
            "completeness_check": "passed" if observed_ids == expected_ids else "failed",
        }
        reporting_agent.assert_privacy_safe(summary)
        return summary

    @staticmethod
    def _safe_input_transaction(transaction: dict[str, Any], transaction_id: str) -> dict[str, Any]:
        metadata = transaction.get("metadata") if isinstance(transaction.get("metadata"), dict) else {}
        return {
            "transaction_id": transaction_id,
            "timestamp": transaction.get("timestamp", ""),
            "source_account": "PRESENT",
            "destination_account": "PRESENT",
            "amount": transaction.get("amount", ""),
            "currency": transaction.get("currency", ""),
            "transaction_type": transaction.get("transaction_type", ""),
            "destination_pattern": "review"
            if str(transaction.get("destination_account", "")).endswith("9999")
            else "standard",
            "metadata": {
                "channel": metadata.get("channel", "unknown"),
                "country": metadata.get("country", "unknown"),
            },
        }
