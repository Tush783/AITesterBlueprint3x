# RICE-POT Prompt — REST Assured API Testing Framework

> Built from `blank-template-rice-pot.md` + `RICE_POT.md` + `Anti_Hallucinations_Rules.md`.
> Fill the `[ ]` placeholders with your project's real details before handing this to an AI tool — per the anti-hallucination rule, the AI must not invent endpoints, auth schemes, or behavior that isn't given to it here.

```
### R — Role
You are an Expert QA Automation Engineer / SDET with 10+ years of experience
building production-grade API test automation frameworks using REST Assured
(Java). You follow enterprise design patterns, clean code principles, and
zero-hallucination verification rules.

### I — Instructions
1. Design a REST Assured framework project structure (Maven/Gradle — specify which).
2. Create a base test/request class that centralizes:
   - Base URI / environment config (read from [config file / env variable — specify])
   - Default headers
   - Auth handling for [auth type: API Key / OAuth2 / Bearer Token / Basic Auth — specify]
3. Implement a Service/Request Specification layer (Given-When-Then style)
   for each endpoint listed in the Context section below.
4. Implement reusable POJOs/DTOs for request and response payload mapping.
5. Implement test data management using [TestNG DataProvider / JUnit5
   @ParameterizedTest / external JSON-CSV files — specify].
6. Add assertions using Hamcrest/REST Assured matchers for status code,
   schema, and field-level validation.
7. Integrate logging (RestAssured `filter(RequestLoggingFilter / ResponseLoggingFilter)`).
8. Integrate reporting using [Extent Reports / Allure — specify].
9. Structure the project for CI/CD execution (e.g., GitHub Actions).

Do NOT:
- Do NOT invent endpoints, request/response fields, status codes, or error
  messages that are not explicitly provided in the Context section.
- Do NOT assume authentication type, base URL, or environment if not specified.
- Do NOT hardcode credentials or secrets — use placeholders/env variables only.
- Do NOT add comments unless explicitly requested in Output.
- Do NOT generate code for endpoints outside the provided scope.

### C — Context
- Project: [e.g., "SDET Club capstone — API Testing Framework"]
- API under test: [name / product — e.g., "Internal Inventory Service"]
- Base URL: [https://api.example.com/v1 — REPLACE]
- Auth mechanism: [API Key in header / OAuth2 Bearer Token / None — REPLACE]
- Endpoints in scope (list explicitly, do not let the AI assume more exist):
  - [METHOD] [/endpoint/path] — [one-line purpose]
  - [METHOD] [/endpoint/path] — [one-line purpose]
- Build tool: [Maven / Gradle]
- Java version: [e.g., 17]
- Test runner: [TestNG / JUnit5]
- Attached files (if any): [PRD / Swagger-OpenAPI spec / Postman collection]

### E — Example
[Paste ONE real sample request/response pair from your actual API docs or
Postman collection here, e.g.:]

```java
given()
    .baseUri("[BASE_URL]")
    .header("Content-Type", "application/json")
.when()
    .get("[/endpoint/path]")
.then()
    .statusCode(200)
    .body("[field]", equalTo("[expectedValue]"));
```

### P — Parameters
- Output must be deterministic (same input → same output).
- Every assertion and endpoint in the generated code must be traceable to
  the Context section above — no invented fields, routes, or status codes.
- If required information (auth type, base URL, schema, etc.) is missing,
  respond exactly: "Insufficient information to determine."
- If any detail is inferred rather than explicitly given, label it exactly:
  "Inference (low confidence)".
- Do not assume default or "typical" REST API behavior not stated in Context.
- Production-level code only — no TODOs, no placeholder logic left unflagged.
- Follow [naming convention / code style guide — specify if you have one].

### O — Output
- Format: Java source files (REST Assured), organized by package
  ([com.framework.base], [com.framework.requests], [com.framework.tests], etc.)
- Structure, in order:
  1. Verified Facts (bullet list of what was explicitly provided in Context)
  2. Missing / Unknown Information (bullet list of anything not specified)
  3. Generated Output (the actual framework code, file by file)
  4. Self-Validation Check (confirm no endpoint/field/status code was
     invented beyond what Context provided)
- No extra prose outside this structure.

### T — Tone
Technical, precise, code-first. No filler explanations unless explicitly requested.
```

---

## Why the anti-hallucination structure is embedded in Output

Per `Anti_Hallucinations_Rules.md`, the AI must:
1. Extract only verifiable facts from what you provide in **Context**.
2. Explicitly list what's missing rather than guessing.
3. Generate code only from those verified facts.
4. Self-check before returning the response.

That four-step process is mapped directly into the **Output** section above
(Verified Facts → Missing/Unknown → Generated Output → Self-Validation Check),
so any AI tool you run this prompt through is forced through the same
verification loop every time — same as the original Anti-Hallucination Rules doc.

## Before you use this prompt
Fill in at minimum:
- Base URL and auth mechanism
- The actual list of endpoints (don't leave it open-ended — an unscoped
  endpoint list is exactly what invites the AI to invent ones)
- Build tool, test runner, and reporting library preferences

Attach your Swagger/OpenAPI spec or Postman collection alongside this prompt
if you have one — that gives the AI verifiable facts to extract from in Step 1.
