# AI Tester Blueprint 3X

A comprehensive learning resource and hands-on automation framework for AI-assisted QA engineering — covering LLM fundamentals, prompt engineering, Selenium UI automation, and REST Assured API testing.

---

## Repository Structure

```
AITESTERBLUEPRINT3X/
├── chapter_01_LLM_Basics/
├── chapter_02_Prompt_Eng/
│   ├── Anti_Hallucinations_Rules.md
│   ├── Notes.md
│   ├── prompting_notes.md
│   ├── RICE_POT.md
│   ├── templates/                    ← 6 fill-in-the-blank prompt templates
│   ├── Project2_Selenium_Framework/
│   │   ├── blank-template-rice-pot.md
│   │   └── AdvancedSeleniumFramework/
│   └── Project3_APITest_Framework/
│       ├── rest-assured-framework-rice-pot-prompt.md
│       └── Rest_assured_API_testing_framework/
└── chapter_03_BLAST_Framework/
    ├── B.L.A.S.T.md               ← the protocol itself
    ├── objective.md
    ├── claude.md                  ← project constitution (schema + rules)
    ├── task_plan.md / findings.md / progress.md
    ├── architecture/              ← Layer 1 SOPs
    ├── backend/                   ← Layer 2 (Navigation) + Layer 3 (Tools)
    └── frontend/                  ← React (Vite) UI
```

---

## Chapter 01 — LLM Basics

Introduction to Large Language Models and foundational concepts.

| File | Purpose |
|------|---------|
| `attention_interactive.html` | Interactive visualization of attention mechanisms |
| `attention_is_all_you_need.html` | Reference material based on the "Attention Is All You Need" paper |

---

## Chapter 02 — Prompt Engineering

### Core Documents

| File | Purpose |
|------|---------|
| `Notes.md` | Study notes and key learnings |
| `prompting_notes.md` | Detailed prompting techniques and best practices |
| `RICE_POT.md` | Full RICE-POT framework reference |
| `Anti_Hallucinations_Rules.md` | Mandatory rules for zero-hallucination AI output |

### Anti-Hallucination Rules

Defines the strict verification process all AI prompts in this project follow:

1. Extract verifiable facts from the provided input only
2. List unknown or missing information explicitly
3. Generate output **only** from verified facts
4. Self-check for hallucinations before returning output

Every project prompt in this repo embeds this four-step loop in its **Output** section.

---

### Prompt Templates (`templates/`)

Ready-to-use, fill-in-the-blank prompts for common QA tasks.

| Template | File | Use When |
|----------|------|----------|
| 01 — Basic Test Case Generation (RTCFR) | `01_TestCaseGeneration+Prompt.md` | You have a feature and want test cases fast |
| 02 — PRD → Comprehensive Test Cases | `02_TestCases_from_prd.md` | You have a PRD/BRD/Jira story to break down |
| 03 — API Test Case Generation | `03_API_TestCase_Generation.md` | You have API docs or a Postman collection |
| 04 — Negative Test Cases Only | `04_Negative_TC_Only.md` | You need to focus solely on error/failure paths |
| 05 — Security Test Cases | `05_Security_Test.md` | You need OWASP Top 10 / security coverage |
| 06 — Regression Test Suite | `06_Regression_Suite.md` | You need a full regression suite with priorities and time estimates |

All templates enforce the anti-hallucination constraints: only documented behavior, explicit "Not specified" for gaps, no invented error codes or endpoints.

---

### Project 2 — Selenium UI Automation Framework

**Location:** `Project2_Selenium_Framework/AdvancedSeleniumFramework/`

A production-grade Selenium/Java framework for Salesforce UI automation, generated using the blank RICE-POT template (`blank-template-rice-pot.md`).

**Stack:** Java 17 · Maven · TestNG · Selenium 4 · ExtentReports · Page Object Model

```
AdvancedSeleniumFramework/
├── pom.xml
├── testng.xml
└── src/
    ├── main/java/com/salesforce/framework/
    │   ├── listeners/ExtentReportListener.java
    │   ├── pages/
    │   │   ├── LoginPage.java
    │   │   └── HomePage.java
    │   └── utils/
    │       ├── DriverFactory.java
    │       └── DriverManager.java
    └── test/java/com/salesforce/tests/
        ├── base/BaseTest.java
        └── login/
            ├── ValidLoginTest.java
            └── InvalidLoginTest.java
```

**Run:**
```bash
mvn clean test -f chapter_02_Prompt_Eng/Project2_Selenium_Framework/AdvancedSeleniumFramework/pom.xml
```

---

### Project 3 — REST Assured API Testing Framework

**Location:** `Project3_APITest_Framework/Rest_assured_API_testing_framework/`

A production-grade REST Assured/Java API test framework generated from the RICE-POT prompt in `rest-assured-framework-rice-pot-prompt.md`.

**Stack:** Java 17 · Maven · TestNG · REST Assured 5.x · ExtentReports · Logback · GitHub Actions CI

**API under test:** [JSONPlaceholder](https://jsonplaceholder.typicode.com) — public, documented REST API (no invented endpoints)

```
Rest_assured_API_testing_framework/
├── pom.xml
├── .gitignore
├── .github/workflows/ci.yml
└── src/
    ├── main/
    │   ├── java/com/framework/
    │   │   ├── config/ConfigManager.java       ← env var → sys prop → config.properties
    │   │   ├── constants/Endpoints.java        ← all route constants
    │   │   └── utils/ExtentReportManager.java  ← dark-theme HTML report singleton
    │   └── resources/
    │       ├── config.properties               ← base URL + auth config (no secrets)
    │       └── logback.xml                     ← console + rolling file logging
    └── test/
        ├── java/com/framework/
        │   ├── base/BaseTest.java              ← RequestSpec, auth wiring, report lifecycle
        │   ├── pojo/
        │   │   ├── Post.java
        │   │   └── User.java
        │   ├── requests/
        │   │   ├── PostsRequest.java           ← GET / POST / PUT / DELETE /posts
        │   │   └── UsersRequest.java           ← GET /users
        │   └── tests/
        │       ├── PostsTest.java              ← 7 tests + DataProvider + JSON schema
        │       └── UsersTest.java              ← 3 tests
        └── resources/
            ├── testng.xml                      ← parallel="methods", thread-count=4
            └── schemas/post_schema.json        ← JSON Schema draft-07 for Post response
```

#### Endpoints covered

| Method | Endpoint | Test |
|--------|----------|------|
| GET | `/posts` | 200 + list of 100 |
| GET | `/posts/{id}` | 200 + field assertions + JSON schema |
| GET | `/posts/999999` | 404 not found |
| POST | `/posts` | 201 + echoed fields (DataProvider × 3) |
| PUT | `/posts/{id}` | 200 + updated fields |
| DELETE | `/posts/{id}` | 200 |
| GET | `/users` | 200 + list of 10 |
| GET | `/users/{id}` | 200 + email format assertion |
| GET | `/users/999999` | 404 not found |

#### Auth configuration

Auth type and token are never hardcoded. Resolution order:

```
Environment variable  →  JVM -D system property  →  config.properties
```

Supported values for `auth.type`: `bearer` · `apikey` · `basic` · _(blank = no auth)_

#### Run locally

```bash
cd chapter_02_Prompt_Eng/Project3_APITest_Framework/Rest_assured_API_testing_framework
mvn clean test
```

Reports are written to `target/reports/ExtentReport_<timestamp>.html`.  
Logs are written to `target/logs/framework.log`.

#### CI/CD (GitHub Actions)

The workflow at `.github/workflows/ci.yml` triggers on push/PR to `main`/`master`:

1. Checks out the repo and sets up JDK 17
2. Runs `mvn clean test` with secrets injected as env variables
3. Uploads the Extent Report and logs as build artifacts

Configure these secrets in your GitHub repo settings:

| Secret | Description |
|--------|-------------|
| `BASE_URL` | Override the default base URL |
| `AUTH_TYPE` | `bearer` / `apikey` / `basic` |
| `AUTH_TOKEN` | The credential value — never commit this |

---

## Chapter 03 — B.L.A.S.T. Framework: ADO Test Plan Generator

**Location:** `chapter_03_BLAST_Framework/`

An end-to-end tool built with the **B.L.A.S.T.** protocol (Blueprint, Link, Architect, Stylize, Trigger): fetches User Stories from an Azure DevOps board by ID and generates a structured, traceable test plan (positive / negative / edge cases per Acceptance Criterion) as a downloadable `.xlsx`.

**Stack:** Python 3 · FastAPI · `azure-devops` SDK · openpyxl · React 18 (Vite)

```
chapter_03_BLAST_Framework/
├── claude.md                      ← Project Constitution: data schema + behavioral rules
├── .env.example                   ← required config keys (copy to .env with real values)
├── architecture/                  ← Layer 1: SOPs for each tool
│   ├── fetch_user_story.md
│   ├── generate_test_plan.md
│   ├── export_xlsx.md
│   └── navigation.md
├── backend/                       ← Layer 2 (Navigation) + Layer 3 (Tools)
│   ├── main.py                    ← FastAPI routes
│   ├── config.py
│   └── tools/
│       ├── ado_client.py          ← fetch a User Story from Azure DevOps
│       ├── test_plan_generator.py ← deterministic, rule-based test case generator (no LLM call)
│       ├── xlsx_export.py         ← writes the final .xlsx deliverable
│       └── verify_connection.py   ← Phase 2 Link handshake script
├── frontend/                      ← Layer 4: lightweight React (Vite) UI
└── run_dev.ps1                    ← one-command local launcher (backend + frontend)
```

Test case generation is intentionally **rule-based, not LLM-based** — Acceptance Criteria are matched against a keyword→scenario table (email, password, permissions, file upload, numeric bounds, dates, etc.) using standard QA test-design heuristics (equivalence partitioning, boundary value analysis), so output stays deterministic and traceable back to the source story.

#### Run locally

```powershell
# 1. Fill in real values
cd chapter_03_BLAST_Framework
copy .env.example .env   # then edit ADO_ORG_URL, ADO_PROJECT, ADO_PAT

# 2. First-time setup
cd backend; python -m venv venv; venv\Scripts\pip install -r requirements.txt; cd ..
cd frontend; npm install; cd ..

# 3. Run both servers
.\run_dev.ps1
```

Backend: `http://127.0.0.1:8000` · Frontend: `http://127.0.0.1:5173`

Sanity-check the Azure DevOps connection before generating anything:
```powershell
backend\venv\Scripts\python backend\tools\verify_connection.py <a_real_story_id>
```

Currently a local-only tool by design — no cloud deployment target has been requested yet.

---

## Getting Started

1. Clone the repo
2. Start with `chapter_01_LLM_Basics/` for foundational knowledge
3. Read `chapter_02_Prompt_Eng/RICE_POT.md` and `Anti_Hallucinations_Rules.md`
4. Use the templates in `templates/` to generate test cases with your AI tool
5. Run Project 2 (Selenium) or Project 3 (REST Assured) with `mvn clean test`
6. Run Chapter 03 (B.L.A.S.T. Test Plan Generator) per the instructions in `chapter_03_BLAST_Framework/`

---

## How to Contribute

- **Report issues:** Open an issue describing the problem or suggestion.
- **Pull requests:** Fork the repo, create a feature branch, and submit a PR.
- **Tests:** Add or update tests when contributing code changes.

## License

This project is released under the MIT License. See `LICENSE.md` for details.

---

**Last Updated:** 2026-07-01
