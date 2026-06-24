# SmartERP — Requirements Document
**Project:** SmartERP (Billing, Inventory & Accounting Management System)
**Stack:** Spring Boot + PostgreSQL
**Phase:** Day 1, Step 1 — Requirement Analysis (MVP Scope)

---

## 1. Purpose

This document defines the **minimum viable product (MVP)** scope for SmartERP, a Tally-inspired
accounting and inventory system. It exists so that every later decision (database design, API
design, code) has a single source of truth to check against.

This is **not** the full Tally-style feature set described in the original brief. It is the
narrowed-down MVP: 5 core flows only. Banking, GST reports, multi-company support, keyboard
shortcuts, and financial reports are explicitly **out of scope** for the MVP and listed under
Future Enhancements.

---

## 2. MVP Core Flows

### 2.1 Login (Authentication)
- User submits **email** and **password**.
- System validates credentials against stored (hashed) password.
- On success, system issues a **JWT token**.
- Token is used to authenticate all subsequent requests.
- On failure, system returns an authentication error (no token issued).

### 2.2 Ledger Management
A **Ledger** represents an account — money owed, owned, or moved.

- User can **create** a ledger with:
  - Name
  - Type: `CUSTOMER`, `SUPPLIER`, `CASH`, `BANK`, `EXPENSE`, or `INCOME`
  - Opening balance
- User can **view** a list of ledgers.
- User can **update** a ledger's details.
- User can **delete** a ledger (only if it has no linked vouchers — to be confirmed in Database Design step).

### 2.3 Stock Item Management
A **Stock Item** represents a product the business buys/sells.

- User can **create** a stock item with:
  - Item name
  - SKU (unique product code)
  - HSN code (tax classification code)
  - Purchase price
  - Selling price
  - Current quantity
  - GST rate (%)
- User can **view** a list of stock items.
- User can **update** stock item details (price, GST rate, etc.).
- User can **delete** a stock item (only if no voucher history exists — to be confirmed).

### 2.4 Purchase Voucher
Represents the business **buying** stock from a supplier.

- User selects a **Supplier Ledger**.
- User adds one or more **Stock Items** with quantity and rate.
- On submission:
  - Each stock item's quantity **increases** by the purchased amount.
  - The supplier ledger's balance **increases** (business owes the supplier more).
  - A voucher record is saved with date, supplier, items, and total amount.

### 2.5 Sales Voucher
Represents the business **selling** stock to a customer.

- User selects a **Customer Ledger**.
- User adds one or more **Stock Items** with quantity and rate.
- On submission:
  - Each stock item's quantity **decreases** by the sold amount.
  - The customer ledger's balance **increases** (customer owes the business).
  - An invoice is generated.
  - A voucher record is saved with date, customer, items, and total amount.

---

## 3. Explicitly Out of Scope (MVP)

These exist in the original SmartERP brief but are **deferred** to post-MVP / future enhancements:

- Multi-company management
- Contra / Payment / Receipt / Journal Vouchers
- Credit Notes / Debit Notes
- GST reports (CGST/SGST/IGST summaries)
- Banking module (cheque tracking, reconciliation)
- Financial reports (Balance Sheet, P&L, Trial Balance, Cash Flow)
- Keyboard-only navigation
- Barcode scanning, OCR, WhatsApp sharing, mobile app

---

## 4. Open Questions (to resolve during Database Design)

- Can a ledger/stock item be deleted if it has existing voucher history, or only deactivated?
- Is each voucher tied to one company (assume yes, for future multi-company support even if MVP is single-company)?
- Do we need a separate `invoices` table, or is the Sales Voucher itself the invoice?

---

## 5. Next Step

→ **Day 1, Step 2: Database Design** — translate these 5 flows into tables, columns, and
relationships (ER diagram + SQL schema).
