---
title: Developer Guide — MeshCRM (AB3 Adaptation)
summary: Target users, value proposition, user stories, representative use cases, non-functional requirements, and glossary for the MeshCRM app.
authors:
  - Team MeshCRM
last_updated: 2025-10-09
---

# Overview

MeshCRM is a lightweight, offline-first contact & follow-up manager for founders and freelancers. It turns every conversation into a next step in under a minute and prevents lead leakage through deterministic cadences and enforced “next action” rules.

## Target Users

- **Ash — student founder**: captures leads at events; needs a focused “what’s next?” list.
- **Ben — recent-grad founder**: imports messy contacts; wants zero lead leakage.
- **Mendy — freelancer**: juggles multi-channel chats; works in bursts; needs snooze/undo.

## Value Proposition

- **One-liner:** *Turn every conversation into a next step—and never let a lead go stale.*
- **Outcomes**
    - Every contact has **exactly one stage** and a **scheduled next action**.
    - **Time-to-first-follow-up ≤ 12 hours** via due-task nudges.
    - **2× weekly follow-ups** completed through deterministic cadences.
    - **Investor update in ~2 minutes** (stages, conversions, response times).

## User Stories (prioritised)

**Must-have**
- As a **user**, I can **add a contact (name/email/note) in ≤60s** so I don’t lose leads.
- As a **user**, I can **set a follow-up cadence (e.g., 3–7–14 days)** so I remember to follow up.
- As a **user**, I can **see a Today list of due follow-ups** so I know what to do now.
- As a **user**, I can **mark follow-ups done or snooze** so my queue stays accurate.
- As a **user**, I can **move a contact through stages (Lead → Pitched → Won/Lost)** to track progress.
- As a **user**, I **must add a “next action” when changing stage** to avoid idle leads.
- As a **user**, I can **view an Overdue list** to rescue stale leads.
- As a **busy user**, I can **search by name/email/company (typo-tolerant)** to find someone fast.
- As a **user**, I can **log an interaction (call/email/meeting)** to remember what was said.
- As a **cautious user**, I can **undo the last action** to fix mistakes.
- As a **user**, I can **archive closed/lost contacts** to focus on active work.

**Nice-to-have (for later)**
- Export a short **progress summary** (investor update).
- **Bulk edits** with diff preview.
- **Local PIN lock** (screen-level protection).

## Representative Use Cases (textual)

### UC-1: Add contact in ≤60s
**Actor:** User  
**Precondition:** App open  
**Main flow:**
1. Trigger **Quick Add**.
2. Enter name and email (or paste text).
3. (Optional) Add 1–2 notes.
4. Select cadence (or accept default).
5. System creates contact in **Lead** stage and schedules first follow-up.  
   **Extensions:**
- 2a. Email missing → allow name-only; mark “email missing”.
- 4a. No cadence chosen → apply default cadence.  
  **Postcondition:** Contact has one stage and a scheduled next action.

### UC-2: Work the **Today** queue
**Actor:** User  
**Main flow:**
1. Open **Today**.
2. System shows due actions ranked by potential + recency.
3. Complete the top action (e.g., send deck).
4. Tap **Done** or **Snooze** (with instant **Undo**).
5. System logs interaction and schedules the next action per cadence.  
   **Postcondition:** Queue updated; metrics feed the summary view.

### UC-3: Advance stage with enforced next action
**Actor:** User  
**Main flow:**
1. Select a new stage (e.g., **Pitched**).
2. System **requires** a next action (date + intent) before confirming.
3. Enter the next action (e.g., “Schedule demo, Fri”).
4. System moves stage and schedules that action.  
   **Postcondition:** No stage change can leave a contact without a next action.

## Non-Functional Requirements (NFRs)

**Performance & responsiveness**
- Add-contact flow completes in **≤ 60s** (keyboard-first).
- Mark-done / snooze / undo latency **< 150 ms** on typical laptops.

**Reliability & correctness**
- **Deterministic cadences**: follow-ups scheduled on exact days (e.g., 3–7–14) until done.
- **Stage invariants**: exactly **one** stage per contact; **every** stage change attaches a next action.

**Usability**
- **Keyboard-first** single-line commands with preview + instant undo.
- **Typo-tolerant search** across name/email/company.

**Security & privacy**
- Optional **local-only PIN lock** (no cloud auth).

**Portability & scope**
- **Single-player, offline-first**; **no auto-sending emails**, **no marketing drip**.
- Minimal domain model: contacts, stages, interactions, cadences.

**Observability**
- Built-in summaries: leads by stage, conversions, response times (for quick investor updates).

## Glossary

- **Contact** — Person/org with name/email and interaction history.
- **Stage** — Pipeline position (Lead, Pitched, Won/Lost); **one** per contact at any time.
- **Next action** — Scheduled follow-up (date + intent) required after any stage change.
- **Cadence** — Deterministic sequence (e.g., **3–7–14 days**) that auto-queues follow-ups until done.
- **Today** — Ranked list of due actions to clear now.
- **Overdue** — Actions past due, used to rescue stale leads.
- **Interaction** — Logged call/email/meeting with timestamp and notes.
- **Evidence note** — Short, concrete signal recorded before stage advance (e.g., “asked for deck”).
- **Investor update** — Auto summary (stages, conversions, reply times).
