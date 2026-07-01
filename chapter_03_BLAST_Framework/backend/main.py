"""Navigation layer (B.L.A.S.T. Layer 2).

Routes requests to the deterministic Layer 3 tools in the right order:
fetch story -> generate test plan -> export .xlsx. Performs no business
logic of its own beyond sequencing and error translation.
"""
from pathlib import Path

from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import FileResponse
from pydantic import BaseModel

from config import OUTPUT_DIR, ConfigError
from tools.ado_client import UserStoryNotFoundError, fetch_user_story
from tools.test_plan_generator import generate_test_plan
from tools.xlsx_export import export_test_plans

app = FastAPI(title="ADO Test Plan Generator")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:5173", "http://127.0.0.1:5173"],
    allow_methods=["*"],
    allow_headers=["*"],
)


class TestPlanRequest(BaseModel):
    story_ids: list[int]


@app.get("/api/health")
def health():
    return {"status": "ok"}


@app.post("/api/test-plan")
def create_test_plan(req: TestPlanRequest):
    if not req.story_ids:
        raise HTTPException(status_code=400, detail="story_ids must contain at least one ID")

    test_plans = []
    errors = []
    for story_id in req.story_ids:
        try:
            story = fetch_user_story(story_id)
        except ConfigError as exc:
            raise HTTPException(status_code=500, detail=str(exc)) from exc
        except UserStoryNotFoundError as exc:
            errors.append(str(exc))
            continue
        test_plans.append(generate_test_plan(story))

    if not test_plans:
        raise HTTPException(status_code=404, detail="; ".join(errors) or "No stories could be fetched")

    xlsx_path = export_test_plans(test_plans, OUTPUT_DIR)

    return {
        "test_plans": test_plans,
        "errors": errors,
        "download_url": f"/api/download/{xlsx_path.name}",
    }


@app.get("/api/download/{filename}")
def download(filename: str):
    path = OUTPUT_DIR / Path(filename).name  # strip any path components to prevent traversal
    if not path.is_file():
        raise HTTPException(status_code=404, detail="File not found")
    return FileResponse(
        path,
        media_type="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        filename=path.name,
    )
