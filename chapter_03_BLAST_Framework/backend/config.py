import os
from pathlib import Path

from dotenv import load_dotenv

ROOT_DIR = Path(__file__).resolve().parent.parent
load_dotenv(ROOT_DIR / ".env")

ADO_ORG_URL = os.getenv("ADO_ORG_URL", "")
ADO_PROJECT = os.getenv("ADO_PROJECT", "")
ADO_PAT = os.getenv("ADO_PAT", "")

BACKEND_HOST = os.getenv("BACKEND_HOST", "127.0.0.1")
BACKEND_PORT = int(os.getenv("BACKEND_PORT", "8000"))

TMP_DIR = Path(__file__).resolve().parent / ".tmp"
OUTPUT_DIR = Path(__file__).resolve().parent / "output"
TMP_DIR.mkdir(exist_ok=True)
OUTPUT_DIR.mkdir(exist_ok=True)


class ConfigError(RuntimeError):
    """Raised when required .env values are missing or still placeholders."""


def require_ado_config() -> None:
    placeholders = {"", "https://dev.azure.com/your-organization", "YourProjectName", "your-personal-access-token-here"}
    missing = [
        name
        for name, value in [("ADO_ORG_URL", ADO_ORG_URL), ("ADO_PROJECT", ADO_PROJECT), ("ADO_PAT", ADO_PAT)]
        if value in placeholders
    ]
    if missing:
        raise ConfigError(
            f"Missing/placeholder Azure DevOps config in .env: {', '.join(missing)}. "
            "Fill in real values before calling the ADO API."
        )
