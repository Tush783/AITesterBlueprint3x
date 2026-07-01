# SOP: Export .xlsx (Layer 3 Tool — `backend/tools/xlsx_export.py`)

## Goal
Write one or more generated test plans to a single `.xlsx` file — the confirmed Delivery Payload (per Phase 1 Blueprint).

## Inputs
- `test_plans: list[dict]` — Output Shape dicts from `test_plan_generator.generate_test_plan`.
- `output_dir: Path` — `backend/output/` (the final, non-ephemeral deliverable location; not `.tmp/`).

## Tool Logic
1. Create a workbook with one sheet, "Test Plan".
2. Header row: Test Case ID, Title, Type, Priority, Preconditions, Steps, Expected Result, Story ID, AC Ref (bold, colored).
3. One row per test case, across all stories in the batch (`steps` list joined with `" | "`).
4. Filename: `test_plan_<story_ids>_<timestamp>.xlsx`, saved into `backend/output/`.
5. Return the saved `Path` so the Navigation layer can build a download URL.

## Edge Cases
- **A story produced zero test cases** (no AC) → it contributes no rows but doesn't break the export; its `flags` are still visible in the JSON API response, just not in the spreadsheet.
- **Very long steps/criteria text** → column widths are capped (min 14, max 60 chars) rather than growing unbounded.
- **Output directory doesn't exist yet** → created automatically (`output_dir.mkdir(exist_ok=True)`).
