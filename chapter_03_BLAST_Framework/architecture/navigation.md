# SOP: Navigation Layer (Layer 2 — `backend/main.py`)

## Goal
Route requests from the React frontend to the three Layer 3 tools in the correct order, translating tool errors into HTTP responses. Contains no business logic of its own.

## Routes
- `GET /api/health` → `{"status": "ok"}` — used by the frontend and by deployment health checks.
- `POST /api/test-plan` body `{"story_ids": [int, ...]}`:
  1. For each `story_id`: call `ado_client.fetch_user_story`. A `ConfigError` (broken Link) aborts the whole request with HTTP 500. A `UserStoryNotFoundError` for one story is collected into `errors` and that story is skipped — the batch continues.
  2. For each successfully fetched story: call `test_plan_generator.generate_test_plan`.
  3. If at least one test plan was generated: call `xlsx_export.export_test_plans` once for the whole batch.
  4. Return `{"test_plans": [...], "errors": [...], "download_url": "/api/download/<file>"}`.
  5. If **no** story could be fetched: HTTP 404 with the collected errors.
- `GET /api/download/{filename}` → streams the `.xlsx` from `backend/output/`. `filename` is reduced to its basename before use to prevent path traversal.

## Edge Cases
- **Mixed batch** (some valid IDs, some invalid) → partial success: valid ones get test plans + are in the xlsx, invalid ones are reported in `errors`, HTTP 200 overall.
- **All IDs invalid** → HTTP 404, no file is written.
- **CORS**: only `http://localhost:5173` / `http://127.0.0.1:5173` (the Vite dev server) are allowed origins.
