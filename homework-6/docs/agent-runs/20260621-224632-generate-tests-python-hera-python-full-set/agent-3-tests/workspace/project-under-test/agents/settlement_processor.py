"""Simulated settlement component for eligible validated transactions."""

from __future__ import annotations

from typing import Any

from .common import (
    COMPONENT_REPORTING,
    COMPONENT_SETTLEMENT,
    REASON_SETTLED,
    STATUS_REJECTED,
    STATUS_REVIEW_REQUIRED,
    STATUS_SETTLED,
    STATUS_VALIDATED,
    append_audit_event,
    append_component_history,
)


def settle_transaction(transaction: dict[str, Any], current_status: str) -> dict[str, Any]:
    if current_status == STATUS_VALIDATED:
        transaction["status"] = STATUS_SETTLED
        transaction["reason_codes"] = [REASON_SETTLED]
        transaction["settlement_reference"] = f"SIM-{transaction.get('transaction_id', 'UNKNOWN')}"
    elif current_status in {STATUS_REJECTED, STATUS_REVIEW_REQUIRED}:
        transaction["status"] = current_status
        transaction.pop("settlement_reference", None)
    else:
        transaction["status"] = STATUS_REJECTED
    return transaction


def process_message(message: dict[str, Any]) -> dict[str, Any]:
    data = message.get("data", {})
    settled = settle_transaction(data, str(data.get("status", STATUS_REJECTED)))
    message["data"] = settled
    message["source_agent"] = COMPONENT_SETTLEMENT
    message["target_agent"] = COMPONENT_REPORTING
    append_component_history(message, COMPONENT_SETTLEMENT)
    reason_codes = settled.get("reason_codes") or [None]
    append_audit_event(message, COMPONENT_SETTLEMENT, settled["status"], reason_codes[0])
    return message
