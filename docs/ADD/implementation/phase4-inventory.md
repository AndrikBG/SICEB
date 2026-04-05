# Phase 4 — Multi-Branch Operations and Inventory

> **Status:** 🔲 Not started  
> **Drivers:** US-071, US-074, US-004, US-004, US-005, US-064, PER-01, ESC-01, ESC-02, ESC-03, CRN-24, CRN-35, CRN-44  
> **Depends on:** Phase 3 complete

**Goal:** Enable full branch management, branch context switching, delta-based inventory mutations (CRN-44), real-time updates, and validate scalability from 3 to 15 branches.

---

## A4.1 — Branch management

- [ ] **T4.1.1** Branch CRUD: create, activate/deactivate, update — new branch operational in under one hour (ESC-01)  
- [ ] **T4.1.2** Branch context switch without logout (ESC-03) — under 3 seconds; local cache purged/partitioned by `branch_id`  
- [ ] **T4.1.3** User assignment to multiple branches — JWT updated with new active branch  
- [ ] **T4.1.4** PWA branch administration views  

## A4.2 — Inventory module

- [ ] **T4.2.1** Data model: `InventoryItem` per branch, linked to Medication or MedicalSupply  
- [ ] **T4.2.2** Delta mutations (CRN-44): DecrementStock, IncrementStock, AdjustStock — never SetStock — deltas with timestamp and `branch_id`  
- [ ] **T4.2.3** Minimum stock thresholds and low-stock alerts  
- [ ] **T4.2.4** Expiration date tracking  
- [ ] **T4.2.5** Full inventory view for General Administrator (US-004) — **all** branches  
- [ ] **T4.2.6** Service-scoped inventory view for Service Manager (US-005) — **only** their service  

## A4.3 — Real-time updates (PER-01)

- [ ] **T4.3.1** Publish inventory events via WebSocket STOMP — changes reflected in under 2 seconds  
- [ ] **T4.3.2** PWA subscription and real-time rendering  
- [ ] **T4.3.3** WebSocket disconnect/reconnect handling  

## A4.4 — Tariff configuration

- [ ] **T4.4.1** Per-service tariffs with DECIMAL(19,4) and `effectiveFrom` (US-064)  
- [ ] **T4.4.2** PWA tariff configuration UI  

## A4.5 — Scalability validation

- [ ] **T4.5.1** Load tests: 3 to 15 branches with &lt;10% degradation (ESC-02)  
- [ ] **T4.5.2** Validate RLS and `branch_id` queries scale linearly  
- [ ] **T4.5.3** Document performance baseline  
- [ ] **T4.5.4** [Carried] Performance: sub-1s patient search with data seeding &gt;50,000 records (deliverable E2.8 deferred)  

---

## Deliverables

- [ ] **E4.1** Branch management — CRUD, context switch &lt;3s, multi-branch  
- [ ] **E4.2** Inventory module — Stock, alerts, expiration, delta commands  
- [ ] **E4.3** Real-time — Inventory reflected &lt;2s via WebSocket (PER-01)  
- [ ] **E4.4** Configurable tariffs — Versioned base price per service  
- [ ] **E4.5** Scalability report — 3→15 branches with &lt;10% degradation  
- [ ] **E4.6** [Carried] Clinical volume tests — Patient search certified sub-1s  

---

## Notes and decisions

<!-- Record decisions, issues, and resolutions during this phase. -->
