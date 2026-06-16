# Prompting Notes

## Why Do We Need Prompting?
We need prompting so that we can get the **desired output** from the LLM — clear instructions = useful results, instead of random or unclear answers.

## Ask vs. Tell

| Approach | Result |
|----------|--------|
| **Ask** ("Could you maybe...?") | ~20% of what the LLM can offer |
| **Tell** ("Do this.") | ~80% — clear, direct commands |

**Rule of thumb:** Don't ask, tell. Once you tell the LLM clearly, *you're in control* — you become the owner of the output, not at the mercy of guesswork.

## How to Unlock 100% of an LLM's Potential
1. **Prompt Engineering** – crafting clear, effective prompts
2. **RAG** (Retrieval-Augmented Generation) – giving the LLM extra reference info to pull from
3. **Fine-tuning** – training the model further on specific data

---

## Core Principles of Prompting

### 1. Be Specific
❌ **Bad:** "Write test cases for login"

✅ **Good:** "Write 5 functional test cases for email/password login, covering valid login, invalid email, invalid password, empty fields, and SQL injection attempt. Use the proper JIRA format: Test ID, Description, Steps, Expected Result."

*The more details and structure you give, the more precise and usable the output.*

### 4. Set Constraints
❌ **Bad:** "Help me test"

✅ **Good:** "Using ONLY the PRD provided, generate test cases. Do NOT assume any features not mentioned. If information is missing, state 'Not specified in PRD'."

*Constraints prevent the LLM from making things up (hallucinating) and keep it grounded in your actual source material.*

> *(Principles 2 & 3 to be added)*

---

## Steps to Follow for Effective Prompt Engineering
1. Define the Goal
2. Gather Context
3. Choose Prompting Strategy
4. Structure the Prompt
5. Add Constraints
6. Test and Iterate
7. Document and Reuse

---

## Example Walkthrough: Create Test Cases for app.vwo.com

### Step 1: Define the Goal
**Ask yourself:**
- What exactly do I need?
- What will I do with the output?
- What does success look like?

❌ **Vague:** "Help me with testing"
✅ **Clear:** "Generate 10 test cases for login validation"

> **Rule 1 — Plan → Generate (AI):** 95% Plan, 5% Action
> *Most of the effort goes into planning the prompt; the AI does the rest.*

**Sample Objective:**
"I have a website which is app.vwo.com. My objective is to write 10 test cases related to valid and invalid scenarios — also called functional test cases — in the JIRA format. Make sure to include all the required columns."

### Step 2: Gather Context
**Collect all relevant information:**
- PRD / Requirements document
- API documentation
- Screenshots / UI mockups
- Error logs
- Previous test cases
- Constraints / Limitations
- JIRA ID, Stories, Epic, BRD, Confluence, Wireframes, Figma, Architecture, Miro, etc.

> **Rule:** More context = Better output

---

### Step 3: Choose Prompting Strategy

#### Zero-Shot
**→** Don't give any example. Mostly used when you want the LLM to do a very simple, well-understood task — like summarizing, fixing grammar, or writing an email.

**Example Prompt:**
> Fix grammar: "Hi sir, I will not be available today. I have a throat infection, so please give me a holiday."

**Output:**
> Hi sir, I will not be available today because I have a throat infection. Please grant me leave for today.
>
> *(If you want, I can also make it more polite or more formal for a manager.)*

#### Few-Shot
**→** Give the LLM one or two examples so it understands the format/pattern you want, then ask it to continue in the same style.

**Example Prompt:**
> Write a test case for app.vwo.com.
>
> First example test case — Test case number one: invalid username and invalid password. If you click submit, you get an error message.
>
> Example number two: valid username and invalid password — you still get an error message after clicking submit. Okay?

**Output:**
> Below are sample test cases for the login functionality of VWO App, written in standard QA test case format:
>
> **Test Cases – Login Page (app.vwo.com)**
>
> **Test Case 1: Login with Invalid Username and Invalid Password**
> - **Test Case ID:** TC_LOGIN_001
> - **Test Scenario:** Verify error message when both username and password are invalid

#### Precise Shot
**→** Give ONE highly detailed, specific prompt — no examples needed — that spells out the exact scenario, the exact output format, and the exact fields you want. Best used when you already know precisely what structure you need (e.g., a bug-linked test case in JIRA format).

**Example Prompt:**
> Write 1 functional test case for the login page of app.vwo.com, in JIRA bug-report format, for this scenario: when a user enters an invalid username/email and/or an invalid password and clicks the Submit button, the system must display an error message. Use exactly these fields: Test Case ID (format VWO-LOGIN-XXX), Title, Precondition, Test Steps (numbered), Test Data, Expected Result, Actual Result, Status, Priority, Severity. Do not include any extra fields or commentary.

**Output:**
> **Test Case ID:** VWO-LOGIN-003
> **Title:** Verify error message on invalid login credentials
> **Precondition:** User is on the app.vwo.com login page
> **Test Steps:**
> 1. Navigate to the app.vwo.com login page
> 2. Enter an invalid username/email and/or an invalid password
> 3. Click the Submit button
> **Test Data:** Email: invalid@test.com, Password: wrongPass123
> **Expected Result:** System displays an error message indicating the entered credentials are invalid
> **Actual Result:** *(to be filled after execution)*
> **Status:** *(Pass/Fail)*
> **Priority:** High
> **Severity:** Major

> *(Steps 4-7 to be added)*
