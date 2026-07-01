# Findings

Research, discoveries, and constraints for the ADO Test Plan Generator project.

## Open Questions
- ~~ADO organization URL and project name~~ — resolved: `.env`/`.env.example` created with `ADO_ORG_URL`, `ADO_PROJECT`, `ADO_PAT` placeholders. Real values still needed from user.
- User initially asked for a "Jira connection file"; clarified via discovery question that the project stays on Azure DevOps only (matches original objective.md/Blueprint). No Jira integration was built.
- `pydantic` had no prebuilt wheel for the local Python 3.14 interpreter at pinned version 2.9.2 (source build failed — PyO3 doesn't support 3.14 yet). Fixed by loosening `requirements.txt` to `pydantic>=2.10`, which resolved to 2.13.4 with a working wheel.

## Build Verification Log
- `backend/tools/verify_connection.py` run against placeholder `.env` → correctly raised `ConfigError` and exited non-zero without a stack trace (Link fails safely).
- `generate_test_plan()` smoke-tested with a synthetic 3-criterion story → produced 9 test cases (3 per criterion: positive/negative/edge), each with correct `traceability`.
- `export_test_plans()` smoke-tested → wrote a valid `.xlsx` to a temp dir.
- FastAPI app started locally: `GET /api/health` → `200 {"status":"ok"}`; `POST /api/test-plan` with a placeholder-config `.env` → `500` with the expected `ConfigError` message (proves the Navigation layer correctly surfaces a broken Link instead of proceeding).

## Research Log
- **microsoft/azure-devops-python-api** (github.com/microsoft/azure-devops-python-api) — official Python SDK. Use `WorkItemTrackingClient.get_work_item(id=...)` to fetch a User Story by ID. Auth via `BasicAuthentication('', PAT)` + `Connection(base_url=org_url, creds=...)`.
- **microsoft/azure-devops-python-samples** — official sample scripts for common ADO operations, useful reference for the `tools/fetch_user_story.py` script.
- Work item fields of interest for a User Story: `System.Title`, `System.Description`, `Microsoft.VSTS.Common.AcceptanceCriteria`, `System.State`, `System.AreaPath`, `System.IterationPath`, `System.Tags`.
- **openpyxl** (Python) is the standard library for writing `.xlsx` — needed for the Excel output tool since delivery = local Excel file.
- Comparable OSS projects (pattern reference, not reused directly): `robleekjr/azure-devops-test-case-generator` (LLM-based PBI → test case generator, exports JSON/CSV) and `guilhermevigneron/Chatgpt_Test_Case_Creator` (prompt-driven test case creation with a CSV-for-Excel export step). Confirms the fetch → LLM-generate → tabular-export pattern is a common, proven shape for this kind of tool.
