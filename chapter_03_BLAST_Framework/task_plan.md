# Task Plan

## Objective
Fetch User Stories from the Azure DevOps (ADO) Board and generate a Test Plan Generator.

## Status
✅ Phase 0 (Initialization)
✅ Phase 1 (Blueprint)
✅ Phase 2 (Link) — code verified locally; real ADO credentials still needed from user
✅ Phase 3 (Architect)
✅ Phase 4 (Stylize)
✅ Phase 5 (Trigger) — local-only, by user choice; no cloud deployment

## Phase 0 Checklist
- [x] Create `task_plan.md`
- [x] Create `findings.md`
- [x] Create `progress.md`
- [x] Initialize `claude.md` (Project Constitution)
- [ ] Discovery Questions answered
- [ ] Data Schema defined in `claude.md`
- [ ] Blueprint approved

## Phase 1 Checklist (Blueprint)
- [x] North Star confirmed — full structured test plan (positive/negative/edge) per story
- [x] Integrations confirmed — Azure DevOps, PAT ready (org/project name still needed, deferred to Phase 2)
- [x] Source of Truth confirmed — specific story IDs, fetched on demand
- [x] Delivery Payload confirmed — local `.xlsx` file
- [x] Behavioral Rules confirmed — standard QA fields + mandatory traceability, no fabricated ACs
- [x] Data Schema (Input/Output JSON) drafted in `claude.md`
- [x] Research: relevant repos/libraries for ADO REST API + test plan generation (see `findings.md`)

## Blueprint Summary (pending your final approval)
- **North Star:** Given one or more ADO User Story IDs, generate a full test plan (positive + negative + edge cases) for each.
- **Integrations:** Azure DevOps only, via `microsoft/azure-devops-python-api`, auth by PAT (stored in `.env`).
- **Source of Truth:** ADO work items, fetched by specific ID on demand (no sprint-wide sweep).
- **Delivery:** One local `.xlsx` file with columns: Test Case ID, Title, Type, Priority, Preconditions, Steps, Expected Result, Story ID, AC Ref.
- **Behavioral Rules:** Standard QA format + mandatory traceability to story/AC; never invent acceptance criteria not present in the story.

**Still needed:** real ADO organization URL, project name, and PAT — fill into `.env` (gitignored, never committed) to make the Link live.

## Phase 2 Checklist (Link)
- [x] `.env` / `.env.example` created with ADO + backend + frontend keys
- [x] `backend/tools/ado_client.py` — fetch one work item by ID, shaped per Input Schema
- [x] `backend/tools/verify_connection.py` — handshake script, verified fails safely on placeholder config
- [ ] Live verification against a real ADO org/PAT (blocked — needs user's real credentials)

## Phase 3 Checklist (Architect)
- [x] `architecture/fetch_user_story.md`, `generate_test_plan.md`, `export_xlsx.md`, `navigation.md` SOPs written
- [x] Layer 3 tools built: `ado_client.py`, `test_plan_generator.py` (deterministic, rule-based — no LLM call), `xlsx_export.py`
- [x] Layer 2 Navigation built: `backend/main.py` (FastAPI) — `/api/health`, `/api/test-plan`, `/api/download/{filename}`
- [x] Smoke-tested locally: generator + exporter + FastAPI endpoints all verified working

## Phase 4 Checklist (Stylize)
- [x] Lightweight React (Vite) app: story ID input, generate button, results table, download link
- [x] Clean minimal styling
- [x] Verified: `npm run build` succeeds; dev server serves correctly; shared root `.env` confirmed readable via `envDir`; CORS preflight against backend confirmed working

## Phase 5 Checklist (Trigger)
- [x] Asked user for a cloud deployment target — answer: **local only for now**
- [x] Local run instructions documented in `claude.md` ("How to Run")
- [x] `run_dev.ps1` added as the local one-command execution trigger
- [ ] Cloud deployment (Azure App Service or other) — deferred until user requests it

## Remaining Before Live Use
- [ ] User fills real `ADO_ORG_URL`, `ADO_PROJECT`, `ADO_PAT` into `.env`
- [ ] Run `backend\venv\Scripts\python tools\verify_connection.py <real_story_id>` to confirm live Link
- [ ] Generate a real test plan through the UI end-to-end
