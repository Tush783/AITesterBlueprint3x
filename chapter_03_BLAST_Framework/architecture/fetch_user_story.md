# SOP: Fetch User Story (Layer 3 Tool — `backend/tools/ado_client.py`)

## Goal
Given a single Azure DevOps work item ID, return it in the Input Shape defined in `claude.md`.

## Inputs
- `story_id: int` — the ADO work item ID.
- `.env`: `ADO_ORG_URL`, `ADO_PROJECT`, `ADO_PAT` (validated by `config.require_ado_config`).

## Tool Logic
1. Load config from `.env`; if any of `ADO_ORG_URL` / `ADO_PROJECT` / `ADO_PAT` is missing or still a placeholder, raise `ConfigError` immediately — do not attempt the network call.
2. Open a `Connection` to `ADO_ORG_URL` using `BasicAuthentication("", ADO_PAT)`.
3. Call `WorkItemTrackingClient.get_work_item(id=story_id, expand="all")`.
4. Strip HTML from `System.Description` and `Microsoft.VSTS.Common.AcceptanceCriteria` (ADO returns these as HTML).
5. Shape the result into the Input Shape dict and return it.

## Edge Cases
- **Work item doesn't exist / wrong project / expired PAT** → `AzureDevOpsServiceError` is caught and re-raised as `UserStoryNotFoundError` with a clear message; the Navigation layer surfaces it per-story without failing the whole batch request.
- **Work item is not a User Story** (e.g. a Bug or Task) → currently fetched anyway; `acceptance_criteria` may be empty, which `test_plan_generator` handles by flagging rather than fabricating.
- **`.env` still has placeholder values** → `ConfigError` raised before any network call (see Phase 2 Link verification).
