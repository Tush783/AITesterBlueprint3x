"""Layer 3 Tool: generate a structured test plan from a User Story.

Deterministic and rule-based on purpose — no LLM call happens inside this
tool. Business logic must be deterministic (per claude.md); the reasoning
about *what makes a good test case* is QA test-design knowledge (positive /
negative / edge, equivalence partitioning, boundary value analysis, error
guessing) encoded directly as rules, not delegated to a model at runtime.

Matches the Output Shape defined in claude.md.
"""
import re
from datetime import datetime, timezone

# (keyword regex, negative case, edge case) — first match wins per criterion.
_KEYWORD_RULES = [
    (r"\bemail\b", "Reject an email address with an invalid format (e.g. missing @ or domain)",
     "Accept an email address at the maximum allowed length"),
    (r"\bpassword\b", "Reject a password shorter than the minimum required length",
     "Accept a password at exactly the maximum allowed length"),
    (r"\b(required|mandatory|must (?:be )?(?:provided|entered))\b", "Submit the form with this field left empty",
     "Submit the field with only whitespace characters"),
    (r"\b(log ?in|sign ?in|authenticat)\w*\b", "Attempt to log in with invalid credentials",
     "Attempt to log in after repeated failed attempts (account lockout boundary)"),
    (r"\b(permission|role|access|authoriz)\w*\b", "Attempt the action as a user without the required permission/role",
     "Attempt the action with a role whose permission was just revoked (stale session)"),
    (r"\b(upload|attach|file)\w*\b", "Upload a file of an unsupported/invalid type",
     "Upload a file at exactly the maximum allowed size"),
    (r"\b(unique|duplicate|already exists)\w*\b", "Submit a value that duplicates an existing record",
     "Submit a value that differs from an existing one only by case/whitespace"),
    (r"\b(number|amount|quantity|price|age|count)\b", "Enter a negative number where a positive value is expected",
     "Enter the value at exactly zero and at the maximum allowed boundary"),
    (r"\b(date|deadline|expir)\w*\b", "Enter a date in the past where a future date is required",
     "Enter a date exactly at the allowed boundary (e.g. today, or the last valid day)"),
    (r"\b(search|filter)\w*\b", "Search with a query that matches no results",
     "Search using special characters / an empty query string"),
    (r"\b(delete|remove)\w*\b", "Attempt to delete an item that no longer exists (already removed)",
     "Attempt to delete without confirming, then verify the cancel path leaves data intact"),
    (r"\b(notif|alert)\w*\b", "Trigger the action when the notification service is unavailable",
     "Verify no duplicate notification is sent on retry"),
]

_HIGH_PRIORITY_KEYWORDS = re.compile(r"\b(password|login|log ?in|permission|role|access|authoriz|security|payment)\w*\b", re.I)

_DEFAULT_NEGATIVE = "Submit unexpected/invalid input and verify the system handles it with a clear error"
_DEFAULT_EDGE = "Submit input at the boundary of what's accepted (empty, minimum, or maximum) and verify graceful handling"


def _split_acceptance_criteria(raw: str) -> list[str]:
    if not raw or not raw.strip():
        return []
    # Split on common list delimiters: newlines, numbered/bulleted markers.
    parts = re.split(r"\n+|(?:^|\s)(?:\d+[\.\)]|[-*•])\s+", raw)
    return [p.strip(" .") for p in parts if p.strip(" .")]


def _match_rule(criterion: str):
    for pattern, negative, edge in _KEYWORD_RULES:
        if re.search(pattern, criterion, re.IGNORECASE):
            return negative, edge
    return _DEFAULT_NEGATIVE, _DEFAULT_EDGE


def _priority_for(criterion: str, case_type: str) -> str:
    if _HIGH_PRIORITY_KEYWORDS.search(criterion):
        return "High"
    return "Medium" if case_type == "positive" else "Low"


def generate_test_plan(story: dict) -> dict:
    """Build the Output Shape test plan for one fetched User Story."""
    story_id = story["id"]
    criteria = _split_acceptance_criteria(story.get("acceptance_criteria", ""))

    test_cases = []
    flags = []

    if not criteria:
        flags.append(
            f"Story {story_id} has no Acceptance Criteria — no test cases were generated. "
            "Add Acceptance Criteria in ADO before this story can get a real test plan."
        )
    else:
        for idx, criterion in enumerate(criteria, start=1):
            ac_ref = f"AC-{idx}"
            negative, edge = _match_rule(criterion)

            test_cases.append({
                "test_case_id": f"TC-{story_id}-{idx:02d}A",
                "title": f"Verify: {criterion}",
                "type": "positive",
                "priority": _priority_for(criterion, "positive"),
                "preconditions": "Preconditions from the story/environment are met",
                "steps": [f"Perform the action described by: {criterion}", "Observe the outcome"],
                "expected_result": f"The system behaves exactly as specified: {criterion}",
                "traceability": {"story_id": story_id, "acceptance_criteria_ref": ac_ref},
            })
            test_cases.append({
                "test_case_id": f"TC-{story_id}-{idx:02d}B",
                "title": f"Negative: {negative}",
                "type": "negative",
                "priority": _priority_for(criterion, "negative"),
                "preconditions": "Preconditions from the story/environment are met",
                "steps": [negative, "Observe the system's response"],
                "expected_result": "The system rejects/handles the invalid case with a clear, appropriate error — no crash or silent data corruption",
                "traceability": {"story_id": story_id, "acceptance_criteria_ref": ac_ref},
            })
            test_cases.append({
                "test_case_id": f"TC-{story_id}-{idx:02d}C",
                "title": f"Edge: {edge}",
                "type": "edge",
                "priority": _priority_for(criterion, "edge"),
                "preconditions": "Preconditions from the story/environment are met",
                "steps": [edge, "Observe the system's response"],
                "expected_result": "The system handles the boundary condition gracefully and consistently with documented limits",
                "traceability": {"story_id": story_id, "acceptance_criteria_ref": ac_ref},
            })

    return {
        "story_id": story_id,
        "story_title": story.get("title", ""),
        "generated_at": datetime.now(timezone.utc).isoformat(),
        "test_cases": test_cases,
        "flags": flags,
    }
