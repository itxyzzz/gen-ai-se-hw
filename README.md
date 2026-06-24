# AI-Assisted Software Engineering Portfolio

This repository contains six completed projects from the **GenAI and Agentic AI for Software Engineering** course. The work focuses on AI-assisted backend prototyping, requirements analysis, agentic workflow design, MCP tooling, testing, documentation, and reviewer-facing evidence preservation.

The repository is organized so reviewers can inspect both the final artifacts and the process evidence behind them: source code, tests, coverage results, PR descriptions, screenshots, run logs, specifications, MCP configuration, planning artifacts, and documented limitations.

## Recommended Review Path

For a fast external review, start here:

1. [Homework 6](homework-6/README.md): capstone Python transaction-processing simulation generated and selected through a role-based multi-agent workflow, with tests, coverage, sanitized outputs, and a read-only MCP status server.
2. [Homework 4](homework-4/README.md): portable text-first agentic bug-fixing pipeline with agents, skills, tool adapters, preserved runs, benchmark artifacts, and a fixed sample app.
3. [Homework 3](homework-3/README.md): finance-oriented EU/EEA dispute-intake specification package showing requirements analysis, scope control, state/workflow modeling, audit/privacy thinking, and agent guidance.
4. [Homework 5](homework-5/README.md): MCP server configuration and custom FastMCP reader service, including project-scoped Codex MCP discovery lessons and focused tests.
5. [Homework 1](homework-1/README.md) and [Homework 2](homework-2/README.md): Java/Spring Boot API foundations, validation, imports, deterministic classification, OpenAPI/Swagger, tests, coverage, lifecycle scripts, and manual verification.

## Portfolio Work Summary

| Stage | What was built or delivered | Evidence reviewers can inspect | Relevant keywords |
|---|---|---|---|
| [HW1: Banking Transactions API](homework-1/README.md) | Java 17/Spring Boot REST API for deposits, withdrawals, transfers, transaction filtering, per-currency balances, and account summaries. Manual Postman checks drove stricter account-direction validation and per-currency aggregation fixes. | Source code, integration tests, demo scripts, Postman/manual verification notes, README/HOWTORUN docs, and [PR #1](https://github.com/itxyzzz/gen-ai-se-hw/pull/1). | Java, Spring Boot, REST API, validation, `BigDecimal`, integration testing |
| [HW2: Intelligent Customer Support API](homework-2/README.md) | Spring Boot support-ticket API with CRUD, filtering, CSV/JSON/XML import, deterministic category/priority classification, Swagger UI, MockMvc/JUnit tests, and JaCoCo coverage above the homework gate. | Source code, fixtures, import samples, MockMvc/JUnit tests, coverage report, API docs, AI usage notes, and [PR #5](https://github.com/itxyzzz/gen-ai-se-hw/pull/5). | Java, Spring Boot, OpenAPI, imports, deterministic classification, JaCoCo |
| [HW3: Dispute Intake Specification](homework-3/README.md) | Documentation-only EU/EEA payment-account dispute-intake package with stakeholder flows, state model, scoped domain rationale, low-level task traceability, operator guidance, and AI-agent instructions. | Specification, domain rules, technical conventions, operator manual, process artifacts, screenshots, and [PR #6](https://github.com/itxyzzz/gen-ai-se-hw/pull/6). | requirements analysis, systems analysis, state modeling, acceptance criteria, audit/privacy documentation |
| [HW4: Agentic Bug-Fixing Pipeline](homework-4/README.md) | Text-first multi-agent workflow launched by `Run HW4 pipeline`, with research, planning, fixing, security review, unit-test generation, tool adapters, preserved run evidence, and benchmark comparison. | Agent specs, skills, adapters, baseline/fixed app, tests, immutable run evidence, benchmark artifacts, screenshots, and [PR #8](https://github.com/itxyzzz/gen-ai-se-hw/pull/8). | agentic workflow, tool adapters, bug fixing, security review, regression testing |
| [HW5: MCP Servers](homework-5/README.md) | GitHub, Filesystem, Notion, and custom FastMCP server configuration. Custom `custom-lorem-reader` exposes a resource and `read` tool with validation tests and screenshot evidence. | MCP configs, custom server, focused tests, screenshots, reviewer setup notes, HOWTORUN guide, and [PR #9](https://github.com/itxyzzz/gen-ai-se-hw/pull/9). | MCP, FastMCP, tool/resource design, credential hygiene, validation testing |
| [HW6: Capstone Python Transaction Pipeline](homework-6/README.md) | Capstone Python transaction-processing simulation selected from multiple AI-generated candidate packages. The workflow separated specification, implementation, test expansion, documentation, package selection, and evidence preservation across role-specific agents. | Runtime pipeline, tests, coverage gate, selected agent-run evidence, research notes, screenshots, sanitized result files, MCP status server, and [PR #10](https://github.com/itxyzzz/gen-ai-se-hw/pull/10). | Python, pytest, coverage gates, multi-agent orchestration, JSON protocol, privacy-aware reporting |

## Skills and Evidence Summary

This portfolio most strongly supports roles around AI-assisted systems/product analysis, workflow automation, AI product engineering, technical business analysis, and prototype-oriented backend/automation work.

| Skill area | Evidence in this repository |
|---|---|
| Requirements and systems analysis | HW3 dispute-intake specification, stakeholder flows, state model, acceptance criteria, scoped domain assumptions, audit/privacy considerations, and task traceability. |
| Backend/API implementation | HW1 and HW2 Spring Boot APIs, REST endpoints, validation, imports, OpenAPI/Swagger docs, JUnit/MockMvc tests, JaCoCo coverage, and manual verification notes. |
| Agentic workflow design | HW4 bug-fixing pipeline and HW6 capstone workflow with planning, implementation, review, testing, documentation, package selection, and preserved run evidence. |
| MCP/tool integration | HW5 GitHub, Filesystem, Notion, and custom FastMCP configuration, custom reader service, validation tests, screenshots, and reviewer runbooks. |
| Verification and evidence discipline | Tests, coverage reports, PR descriptions, screenshots, changelogs, sanitized outputs, run artifacts, planning records, and documented limitations. |

## AI Assistance And Operator Role

The repository was built as an AI-assisted course portfolio by Igor Tanatarov. Codex was the primary environment across the work, with Google Antigravity, Open Code, ChatGPT Deep Research, Context7, MCP tooling, and named homework automation agents used in specific stages. The PR descriptions and homework README files document where AI generated drafts, where the operator redirected scope, where manual checks found issues, and where validation evidence was preserved.

The most important authorship signal is not that every artifact was hand-written. It is that the work repeatedly records how AI output was planned, constrained, corrected, verified, documented, and safely framed.

## Scope And Claim Boundaries

This is a coursework portfolio, not a production banking, compliance, fraud, AML/KYC, PCI, or cloud/SRE system. The strongest evidence is AI-assisted engineering judgment: scoping work, directing AI tools, validating outputs, correcting errors, preserving evidence, writing specifications, and packaging work for review.

Claims supported by this repository:

- Completed a six-stage AI-assisted software engineering course portfolio.
- Built local Java/Spring Boot APIs and a Python transaction-processing simulation with tests and documentation.
- Produced a finance-oriented requirements/specification package with careful scope and claim boundaries.
- Designed agentic and MCP-based workflows with preserved run evidence and reviewer instructions.
- Demonstrated documentation, testing, handoff, evidence, and AI-workflow governance discipline.

Claims not supported without additional evidence:

- Production banking, payments, settlement, fraud, AML/KYC, sanctions, PCI, PSD2/GDPR/DORA implementation, or compliance automation.
- ML model development, model training, fine-tuning, RAG, formal AI evaluations, cloud deployment, SRE, observability, or high-availability operations.
- Sole manual authorship of all artifacts without acknowledging AI assistance and course scaffolding.

---

## Original Course Assignment README

The content below is the original root homework/assignment README formulation preserved for course context.

# 🤖 AI-Assisted Development Course — Homework Repository

Welcome to the homework repository for the **GenAI and Agentic AI for Software Engineering** training course! This repository serves as a template for submitting your homework assignments throughout the program.

> 💡 **Pro Tip**: Star this repository to easily find it later!

---

## 🚀 Getting Started

### Step 1: Fork This Repository

1. Click the **Fork** button in the top-right corner of this repository page
2. Select your personal GitHub account as the destination
3. Wait for GitHub to create your personal copy of the repository

### Step 2: Clone Your Fork

```bash
git clone https://github.com/YOUR_USERNAME/ai-assisted-dev-homework.git
```

---

## 📁 Repository Structure

```
ai-assisted-dev-homework/
├── 📄 README.md                    # This file
├── 📂 homework-1/                  # Homework 1: Simple API with AI Assistance
│   ├── 📄 README.md               # Your documentation for HW1
│   ├── 📂 src/                    # Your source code
│   ├── 📂 docs/                   # Additional documentation
│   │   └── 📂 screenshots/        # Screenshots demonstrating AI usage
│   └── 📂 demo/                   # Demo files and run scripts
├── 📂 homework-2/                  # Homework 2: Enhanced App with Tests
├── 📂 homework-3/                  # Homework 3: App from Specification
├── 📂 homework-4/                  # Homework 4: Multi-Agent System
├── 📂 homework-5/                  # Homework 5: MCP Server Configuration
└── 📂 homework-6/                  # Homework 6: Capstone Project
```

---

## 📤 How to Submit Your Homework

### 1️⃣ Create a Branch for Each Assignment

```bash
# For homework 1
git checkout -b homework-1-submission

# Work on your assignment...

git add .
git commit -m "Complete homework 1"
git push origin homework-1-submission
```

### 2️⃣ Create a Pull Request

> **Pull request quality:** Do not submit a bare or one-line PR. Prepare a **detailed pull request** with a full description of what you implemented, how you used AI, how reviewers can run and verify your work, and **screenshots** (or other demos) where the homework asks for evidence. Homework submitted **without a proper PR description** and the expected visual documentation **will be rejected**. Treat the PR body as the primary submission narrative—link to `README.md` / `HOWTORUN.md` in the repo, but the PR itself must still stand on its own.

1. Go to your forked repository on GitHub. *Do not create pull requests into original repository*
2. Click **"Compare & pull request"** or go to **Pull requests** → **New pull request**
3. Set the base repository to the **your personal repository**
4. Set the base branch to `main`
5. Set the compare branch to your `homework-X-submission` branch
6. Fill in the PR template with a **thorough** write-up, including:
   - ✅ Summary of what you implemented (enough detail for someone unfamiliar with your branch)
   - 🛠️ AI tools used (prompts, workflow, what you verified yourself)
   - ⚠️ Challenges encountered and how you addressed them
   - 📸 **Screenshots** showing the running solution and, where required, AI-assisted work—embed key images in the PR.  Also add them to `docs/screenshots/`

📌 Home Work without detailed description and screenshots in PR will not be accepted. 

### 3️⃣ Assign the Instructor for Review

1. In the Pull Request, click **"Reviewers"** on the right sidebar
2. Search for and add the instructor's GitHub username Alexey-Popov
3. Optionally add labels like `homework-1`, `ready-for-review`

---

## 📋 Submission Requirements

Each homework submission **MUST** include a merge-ready **pull request** on your fork that matches the expectations under **Create a Pull Request** (detailed description, how to verify, and screenshots or demos as required). **Insufficient PR descriptions are grounds for rejection**, even if the code is present in the branch.

Each homework submission **MUST** also include in the repository:

### 📝 Required Documentation

| Item | Description |
|------|-------------|
| `README.md` | Clear explanation of your solution, approach, and AI tools used and *author* |
| `HOWTORUN.md` | Step-by-step guide to run your application |

### 📸 Screenshots *(Highly Expected)*

Include screenshots demonstrating:
- 🤖 AI tool interactions (prompts and responses)
- ✅ Your application running successfully
- 🧪 Test results (if applicable)
- 💡 Any interesting AI suggestions or corrections

> 📁 Place screenshots in the `docs/screenshots/` folder within each homework directory.

> 🎬 Provide runnable demo scripts where applicable.

---

## ▶️ How to Run Applications

Each homework folder should contain clear instructions. 

🔐 Environment Setup should be detailed to run the application.

Add 🧪 Testing guide.

---

## 📊 Grading Criteria

Your submissions will be evaluated on:

| Criteria | Weight | Description |
|----------|--------|-------------|
| ⚙️ **Functionality** | 30% | Does the code work as specified? |
| 📝 **AI Usage Documentation** | 25% | Clear documentation of how AI tools were used |
| 💻 **Code Quality** | 20% | Clean, readable, well-structured code |
| 📚 **Documentation** | 15% | README, comments, and explanations |
| 🎬 **Demo & Screenshots** | 10% | Visual evidence of working solution and AI interaction |

---

**🆘 Getting Help**

Contact the 📚instructor.

Collaborate with 👥 classmates (but submit individual work).

---


<div align="center">

### 🌟 Good luck with your assignments!

</div>
