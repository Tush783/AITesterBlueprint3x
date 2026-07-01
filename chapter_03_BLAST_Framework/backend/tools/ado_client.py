"""Layer 3 Tool: fetch a single User Story from Azure DevOps by ID.

Deterministic, atomic, testable. Matches the Input Shape defined in claude.md.
"""
import re

from azure.devops.connection import Connection
from azure.devops.exceptions import AzureDevOpsServiceError
from msrest.authentication import BasicAuthentication

from config import ADO_ORG_URL, ADO_PAT, require_ado_config


class UserStoryNotFoundError(RuntimeError):
    pass


def _strip_html(raw: str) -> str:
    if not raw:
        return ""
    text = re.sub(r"<[^>]+>", " ", raw)
    text = text.replace("&nbsp;", " ").replace("&amp;", "&")
    return re.sub(r"\s+", " ", text).strip()


def _get_client():
    require_ado_config()
    credentials = BasicAuthentication("", ADO_PAT)
    connection = Connection(base_url=ADO_ORG_URL, creds=credentials)
    return connection.clients.get_work_item_tracking_client()


def fetch_user_story(story_id: int) -> dict:
    """Fetch one User Story work item and shape it per the Input Shape schema."""
    client = _get_client()
    try:
        work_item = client.get_work_item(id=story_id, expand="all")
    except AzureDevOpsServiceError as exc:
        raise UserStoryNotFoundError(f"ADO work item {story_id} could not be fetched: {exc}") from exc

    fields = work_item.fields
    return {
        "id": work_item.id,
        "title": fields.get("System.Title", ""),
        "description": _strip_html(fields.get("System.Description", "")),
        "acceptance_criteria": _strip_html(fields.get("Microsoft.VSTS.Common.AcceptanceCriteria", "")),
        "state": fields.get("System.State", ""),
        "area_path": fields.get("System.AreaPath", ""),
        "iteration_path": fields.get("System.IterationPath", ""),
        "tags": [t.strip() for t in fields.get("System.Tags", "").split(";") if t.strip()],
        "url": work_item.url,
    }
