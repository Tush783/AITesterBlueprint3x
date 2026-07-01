"""Phase 2 (Link) handshake script.

Run directly: `python tools/verify_connection.py <story_id>`
Confirms .env is populated with real Azure DevOps credentials and that a
work item can actually be fetched, before any full logic is built on top.
"""
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # backend/ for `config`

from ado_client import UserStoryNotFoundError, fetch_user_story
from config import ConfigError


def main() -> int:
    if len(sys.argv) < 2:
        print("Usage: python tools/verify_connection.py <story_id>")
        return 1

    story_id = int(sys.argv[1])
    try:
        story = fetch_user_story(story_id)
    except ConfigError as exc:
        print(f"[LINK BROKEN] Config error: {exc}")
        return 1
    except UserStoryNotFoundError as exc:
        print(f"[LINK BROKEN] ADO request failed: {exc}")
        return 1

    print("[LINK OK] Connected to Azure DevOps and fetched work item:")
    print(f"  ID: {story['id']}")
    print(f"  Title: {story['title']}")
    print(f"  State: {story['state']}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
