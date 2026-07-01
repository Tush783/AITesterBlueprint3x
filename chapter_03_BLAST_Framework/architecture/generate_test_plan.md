# SOP: Generate Test Plan (Layer 3 Tool — `backend/tools/test_plan_generator.py`)

## Goal
Given one fetched User Story, deterministically produce positive, negative, and edge test cases covering every Acceptance Criterion, matching the Output Shape in `claude.md`.

## Why Deterministic (No LLM Call)
Per `claude.md`'s Architectural Invariants, Layer 3 tools must be deterministic — LLMs are probabilistic and business logic must not be. Test-case design knowledge (equivalence partitioning, boundary value analysis, error guessing) is encoded directly as rules in this file, not generated live by a model.

## Inputs
- `story: dict` — the Input Shape dict from `ado_client.fetch_user_story`.

## Tool Logic
1. Split `acceptance_criteria` into individual criteria on newlines / numbered / bulleted list markers.
2. For each criterion (`AC-<n>`):
   - **Positive case**: "Verify: `<criterion>`" — always generated, priority Medium (High if a security/auth keyword is present).
   - **Negative case**: matched against a keyword→scenario table (email, password, required field, login, permission, file upload, uniqueness, numeric bounds, dates, search, delete, notifications). Falls back to a generic "invalid input" case if no keyword matches.
   - **Edge case**: paired with the same keyword match — boundary length, boundary value, or boundary timing scenario. Falls back to a generic boundary/empty-input case.
3. Every test case carries `traceability: {story_id, acceptance_criteria_ref}` pointing back to the specific AC.
4. If a story has **no** Acceptance Criteria, generate **zero** test cases and add a `flags` entry instead of inventing scope (per the "Do Not fabricate" Behavioral Rule).

## Edge Cases
- **AC text has no delimiters** (single paragraph) → treated as one criterion; still yields 3 test cases.
- **AC mentions multiple keywords** (e.g. "unique email required") → first matching rule in the table wins (email, in this example).
- **Empty/whitespace-only AC** → same as "no Acceptance Criteria": flagged, no fabricated cases.

## Extending the Rule Table
To add a new keyword → scenario pair, append to `_KEYWORD_RULES` in `test_plan_generator.py`. Do not add rules that fabricate business logic not implied by the keyword itself.
