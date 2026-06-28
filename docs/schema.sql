-- ============================================================
-- SmartERP — Database Schema
-- Day 1, Step 2: Database Design (MVP Scope)
-- Stack: PostgreSQL
-- ============================================================
-- Design decisions locked in during Step 2:
--   1. Soft delete everywhere      -> is_active flag, never hard DELETE
--   2. company_id on every table   -> future-proofs multi-company support
--   3. Voucher and Invoice are separate tables
--   4. Primary keys are BIGSERIAL (simple auto-incrementing integers)
-- ============================================================


-- ============================================================
-- 1. COMPANIES
-- Top-level tenant. Every other table belongs to a company.
-- ============================================================
CREATE TABLE companies (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(150) NOT NULL,
    gst_number      VARCHAR(20),
    financial_year  VARCHAR(9),              -- e.g. '2025-2026'
    address         VARCHAR(255),
    state           VARCHAR(100),
    contact_number  VARCHAR(20),
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP NOT NULL DEFAULT NOW()
);


-- ============================================================
-- 2. USERS
-- Login credentials. A user can belong to / manage a company.
-- ============================================================
CREATE TABLE users (
    id              BIGSERIAL PRIMARY KEY,
    email           VARCHAR(150) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    full_name       VARCHAR(150),
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

-- ============================================================
-- 2b. USER_COMPANIES
-- Join table: one user can own up to 5 companies.
-- ============================================================
CREATE TABLE user_companies (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(id),
    company_id  BIGINT NOT NULL REFERENCES companies(id),
    role        VARCHAR(50) NOT NULL DEFAULT 'OWNER',
    is_active   BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (user_id, company_id)
);


-- ============================================================
-- 3. LEDGERS
-- Accounts: Customer, Supplier, Cash, Bank, Expense, Income.
-- ============================================================
CREATE TABLE ledgers (
    id               BIGSERIAL PRIMARY KEY,
    company_id       BIGINT NOT NULL REFERENCES companies(id),
    name             VARCHAR(150) NOT NULL,
    type             VARCHAR(20) NOT NULL
                      CHECK (type IN ('CUSTOMER', 'SUPPLIER', 'CASH', 'BANK', 'EXPENSE', 'INCOME')),
    opening_balance  NUMERIC(15,2) NOT NULL DEFAULT 0,
    current_balance  NUMERIC(15,2) NOT NULL DEFAULT 0,
    gst_number       VARCHAR(20),
    address          VARCHAR(255),
    contact_number   VARCHAR(20),
    is_active        BOOLEAN NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_ledgers_company ON ledgers(company_id);


-- ============================================================
-- 4. STOCK_ITEMS
-- Products bought/sold. Tracks live quantity.
-- ============================================================
CREATE TABLE stock_items (
    id              BIGSERIAL PRIMARY KEY,
    company_id      BIGINT NOT NULL REFERENCES companies(id),
    name            VARCHAR(150) NOT NULL,
    sku             VARCHAR(50) NOT NULL,
    hsn_code        VARCHAR(20),
    purchase_price  NUMERIC(15,2) NOT NULL DEFAULT 0,
    selling_price   NUMERIC(15,2) NOT NULL DEFAULT 0,
    quantity        NUMERIC(15,2) NOT NULL DEFAULT 0,
    gst_rate        NUMERIC(5,2) NOT NULL DEFAULT 0,   -- percentage, e.g. 18.00
    unit            VARCHAR(20),                        -- PCS, BOX, KG, LTR
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (company_id, sku)
);

CREATE INDEX idx_stock_items_company ON stock_items(company_id);


-- ============================================================
-- 5. VOUCHERS
-- Header record for a Purchase or Sales transaction.
-- ledger_id = the supplier (if PURCHASE) or customer (if SALES).
-- ============================================================
CREATE TABLE vouchers (
    id              BIGSERIAL PRIMARY KEY,
    company_id      BIGINT NOT NULL REFERENCES companies(id),
    voucher_type    VARCHAR(20) NOT NULL
                      CHECK (voucher_type IN ('PURCHASE', 'SALES')),
    voucher_no      VARCHAR(50) NOT NULL,
    voucher_date    DATE NOT NULL DEFAULT CURRENT_DATE,
    ledger_id       BIGINT NOT NULL REFERENCES ledgers(id),
    total_amount    NUMERIC(15,2) NOT NULL DEFAULT 0,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (company_id, voucher_type, voucher_no)
);

CREATE INDEX idx_vouchers_company ON vouchers(company_id);
CREATE INDEX idx_vouchers_ledger ON vouchers(ledger_id);


-- ============================================================
-- 6. VOUCHER_ITEMS
-- Line items per voucher (one row per stock item in a voucher).
-- ============================================================
CREATE TABLE voucher_items (
    id              BIGSERIAL PRIMARY KEY,
    voucher_id      BIGINT NOT NULL REFERENCES vouchers(id) ON DELETE CASCADE,
    stock_item_id   BIGINT NOT NULL REFERENCES stock_items(id),
    quantity        NUMERIC(15,2) NOT NULL,
    rate            NUMERIC(15,2) NOT NULL,
    amount          NUMERIC(15,2) NOT NULL,   -- quantity * rate
    created_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_voucher_items_voucher ON voucher_items(voucher_id);
CREATE INDEX idx_voucher_items_stock_item ON voucher_items(stock_item_id);


-- ============================================================
-- 7. INVOICES
-- Printable document linked to a SALES voucher.
-- Kept separate from vouchers so future invoice types
-- (proforma, quotation) can reuse this table.
-- ============================================================
CREATE TABLE invoices (
    id              BIGSERIAL PRIMARY KEY,
    company_id      BIGINT NOT NULL REFERENCES companies(id),
    voucher_id      BIGINT NOT NULL REFERENCES vouchers(id),
    invoice_number  VARCHAR(50) NOT NULL,
    invoice_date    DATE NOT NULL DEFAULT CURRENT_DATE,
    invoice_type    VARCHAR(20) NOT NULL DEFAULT 'GST'
                      CHECK (invoice_type IN ('GST', 'PROFORMA', 'QUOTATION', 'ESTIMATE')),
    total_amount    NUMERIC(15,2) NOT NULL DEFAULT 0,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (company_id, invoice_number)
);

CREATE INDEX idx_invoices_voucher ON invoices(voucher_id);


-- ============================================================
-- End of MVP schema.
-- Next: Day 2 — Spring Boot Setup + PostgreSQL Setup
-- (translate these tables into JPA @Entity classes)
-- ============================================================
