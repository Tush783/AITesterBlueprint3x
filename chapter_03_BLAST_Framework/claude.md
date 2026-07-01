# claude.md — Project Constitution

Status: DRAFT — awaiting Phase 1 Discovery answers before the schema and rules below are finalized.

## Project
ADO Test Plan Generator — fetches User Stories from an Azure DevOps board and generates a structured Test Plan from each.

## Data Schema (Input/Output Payload)

### Input Shape (raw User Story from ADO, per story ID)
```json
{
  "id": 12345,
  "title": "As a user, I can reset my password",
  "description": "<html/plain description from System.Description>",
  "acceptance_criteria": "<html/plain from Microsoft.VSTS.Common.AcceptanceCriteria>",
  "state": "Active",
  "area_path": "ProjectX\\TeamA",
  "iteration_path": "ProjectX\\Sprint 12",
  "tags": ["auth", "security"],
  "url": "https://dev.azure.com/{org}/{project}/_apis/wit/workItems/12345"
}
```

### Output Shape (generated Test Plan, per story)
```json
{
  "story_id": 12345,
  "story_title": "As a user, I can reset my password",
  "generated_at": "2026-07-01T00:00:00Z",
  "test_cases": [
    {
      "test_case_id": "TC-12345-01",
      "title": "Valid password reset via email link",
      "type": "positive",
      "priority": "High",
      "preconditions": "User account exists and is active",
      "steps": ["Navigate to login page", "Click 'Forgot password'", "..."],
      "expected_result": "User receives reset email and can set a new password",
      "traceability": {
        "story_id": 12345,
        "acceptance_criteria_ref": "AC-1"
      }
    }
  ]
}
```
- `test_cases[].type` ∈ `positive | negative | edge`.
- Every `test_case` MUST carry a `traceability` block pointing back to the source story ID and, where possible, the specific acceptance criterion it covers (per user's "Standard + traceability" format choice).
- Final on-disk artifact: one `.xlsx` file per run (or per story — TBD in Architect phase), sheet columns = Test Case ID, Title, Type, Priority, Preconditions, Steps, Expected Result, Story ID, AC Ref.

## Behavioral Rules
- **Format:** Standard QA test case fields (ID, Title, Preconditions, Steps, Expected Result, Priority) **plus mandatory traceability** back to the ADO Acceptance Criteria / Story ID.
- **Coverage:** Each story must yield positive, negative, and edge-case scenarios — not positive-only.
- **Do Not:** Never fabricate acceptance criteria that aren't present in the story; if a story has no acceptance criteria, flag it rather than inventing scope.
- **Source scope:** Stories are fetched by specific ID(s) provided by the user on demand — no automatic sprint-wide sweep.
- **Delivery:** Output is a local `.xlsx` file (not written back to ADO).

## Architectural Invariants
- 3-layer A.N.T. architecture: `architecture/` (SOPs) → Navigation (`backend/main.py`, FastAPI) → `tools/` (deterministic Python scripts in `backend/tools/`).
- No coding in `tools/` until Discovery + Data Schema + approved Blueprint exist.
- `.env` holds all secrets (ADO PAT, etc.), never hardcoded. See `.env.example` for required keys.
- `backend/.tmp/` is the intermediate workbench; ephemeral only, gitignored.
- `backend/output/` holds the final delivered `.xlsx` payloads — not ephemeral, this is where "Complete" is measured from.
- **Test-case generation is rule-based, not LLM-based**: `test_plan_generator.py` encodes QA test-design heuristics (positive/negative/edge, keyword→scenario table) directly as deterministic code. No model call happens inside any Layer 3 tool, consistent with "LLMs are probabilistic; business logic must be deterministic."
- Backend: Python/FastAPI. Frontend: lightweight React (Vite). CORS restricted to the Vite dev server origin.

## Implemented Components (Phase 2 & 3)
- `backend/config.py` — loads `.env`, validates ADO config isn't still placeholder values.
- `backend/tools/ado_client.py` — fetches one User Story by ID (see `architecture/fetch_user_story.md`).
- `backend/tools/verify_connection.py` — Phase 2 Link handshake script (`python tools/verify_connection.py <id>`).
- `backend/tools/test_plan_generator.py` — deterministic test case generator (see `architecture/generate_test_plan.md`).
- `backend/tools/xlsx_export.py` — writes the final `.xlsx` deliverable (see `architecture/export_xlsx.md`).
- `backend/main.py` — Navigation layer / FastAPI routes (see `architecture/navigation.md`).

## How to Run (local — Phase 5: Trigger, local-only per user decision)
1. Fill real values into `.env` (`ADO_ORG_URL`, `ADO_PROJECT`, `ADO_PAT`).
2. First-time setup: `cd backend && python -m venv venv && venv\Scripts\pip install -r requirements.txt`, and `cd frontend && npm install`.
3. Run both dev servers with one command: `.\run_dev.ps1` (from `chapter_03_BLAST_Framework/`) — opens backend on :8000 and frontend on :5173.
4. Or run manually: `backend\venv\Scripts\uvicorn main:app --reload --port 8000` and, separately, `npm run dev` in `frontend/`.
5. Sanity check the Link before generating anything: `backend\venv\Scripts\python tools\verify_connection.py <a_real_story_id>`.

No cloud deployment target has been requested yet — this stays a local tool until the user asks for one (Azure App Service was suggested and declined for now).

## Maintenance Log
- 2026-07-01: File initialized (Protocol 0).
- 2026-07-01: Renamed `LLM.md` → `claude.md` (also doubles as this project's `CLAUDE.md` on case-insensitive filesystems, so it's auto-loaded as instructions in future sessions — fitting, since this file is meant to be "law").
- 2026-07-01: Phase 2 (Link) complete — `.env`/`.env.example` created, ADO client built, handshake script verified to fail safely (clear `ConfigError`, no stack trace) against placeholder credentials. **Real ADO org/project/PAT still required from user before live fetch works.**
- 2026-07-01: Phase 3 (Architect) complete — SOPs written, Navigation + all 3 Tools built and smoke-tested locally (generator produced 9 correctly-typed test cases from a 3-criterion synthetic story; FastAPI `/api/health` and `/api/test-plan` verified end-to-end).
- 2026-07-01: Phase 4 (Stylize) complete — lightweight React (Vite) frontend built (`frontend/`), reads shared root `.env` via `envDir` (verified by round-tripping a test value through a rebuild). `npm run build` succeeds; backend+frontend dev servers verified running together with working CORS preflight.
- 2026-07-01: Phase 5 (Trigger) — user chose **local-only**, no cloud deployment. Added `run_dev.ps1` as the local execution trigger and a "How to Run" section above. Project is feature-complete pending the user filling in real ADO credentials in `.env`.
