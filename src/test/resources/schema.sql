-- H2 Database Schema for Integration Tests

-- Create schema
CREATE SCHEMA IF NOT EXISTS sales_scm;

-- Drop tables if exist
DROP TABLE IF EXISTS sales_scm.sbl_estimate_item CASCADE;
DROP TABLE IF EXISTS sales_scm.sbl_estimate CASCADE;
DROP TABLE IF EXISTS sales_scm.sbl_colledger CASCADE;
DROP TABLE IF EXISTS sales_scm.sbl_demand CASCADE;

-- sbl_demand (청구마스터)
CREATE TABLE sales_scm.sbl_demand (
    demand_id VARCHAR(255) PRIMARY KEY,
    demand_dt DATE NOT NULL,
    cust_cd VARCHAR(255) NOT NULL,
    demand_start_dt DATE NOT NULL,
    demand_stnd_dt DATE NOT NULL,
    stnd_price DECIMAL(19, 2) NOT NULL,
    supval DECIMAL(19, 2) NOT NULL,
    demand_charge DECIMAL(19, 2) NOT NULL,
    addtax DECIMAL(19, 2) NOT NULL,
    dscnt_rate DECIMAL(19, 4) NOT NULL,
    demand_create_dtime TIMESTAMP NOT NULL,
    demand_creator_emp_no VARCHAR(255),
    insu_price DECIMAL(19, 2),
    invc_output_dtime TIMESTAMP,
    invc_output_empno VARCHAR(255),
    slstmt_no VARCHAR(255),
    slstmt_send_dt DATE,
    slstmt_send_emp_no VARCHAR(255),
    demand_memo TEXT,
    sap_cust_cd VARCHAR(255),
    bill_publ_yn BOOLEAN NOT NULL DEFAULT FALSE,
    invc_recp_email_addr VARCHAR(255),
    exrt_id BIGINT,
    creator VARCHAR(255) NOT NULL,
    create_dtime TIMESTAMP NOT NULL,
    updater VARCHAR(255) NOT NULL,
    update_dtime TIMESTAMP NOT NULL
);

-- sbl_colledger (청구수금원장)
CREATE TABLE sales_scm.sbl_colledger (
    colledger_id VARCHAR(255) PRIMARY KEY,
    colbill_div_cd VARCHAR(50) NOT NULL,
    colbill_dt DATE NOT NULL,
    cust_cd VARCHAR(50) NOT NULL,
    colbill_item_nm VARCHAR(50) NOT NULL,
    colbill_item_dtl VARCHAR(200),
    colbill_amt DECIMAL(19, 2) NOT NULL DEFAULT 0,
    colbill_memo VARCHAR(4000),
    creator VARCHAR(50) NOT NULL,
    create_dtime TIMESTAMP NOT NULL,
    updater VARCHAR(50) NOT NULL,
    update_dtime TIMESTAMP NOT NULL
);

-- sbl_estimate (견적서/거래명세서)
CREATE TABLE sales_scm.sbl_estimate (
    estimate_id VARCHAR(255) PRIMARY KEY,
    doc_type VARCHAR(10) NOT NULL,
    doc_no VARCHAR(255) NOT NULL,
    reg_dt DATE NOT NULL,
    title VARCHAR(500) NOT NULL,
    receiver VARCHAR(255),
    reference VARCHAR(255),
    writer_emp_no VARCHAR(255),
    dept_cd VARCHAR(255),
    remark TEXT,
    note TEXT,
    total_supval DECIMAL(19, 2) NOT NULL DEFAULT 0,
    total_addtax DECIMAL(19, 2) NOT NULL DEFAULT 0,
    total_amt DECIMAL(19, 2) NOT NULL DEFAULT 0,
    creator VARCHAR(255) NOT NULL,
    create_dtime TIMESTAMP NOT NULL,
    updater VARCHAR(255) NOT NULL,
    update_dtime TIMESTAMP NOT NULL
);

-- sbl_estimate_item (견적서 항목)
CREATE TABLE sales_scm.sbl_estimate_item (
    estimate_item_id VARCHAR(255) PRIMARY KEY,
    estimate_id VARCHAR(255) NOT NULL,
    item_nm VARCHAR(500) NOT NULL,
    qty DECIMAL(19, 2) NOT NULL,
    unit_price DECIMAL(19, 2) NOT NULL,
    supval DECIMAL(19, 2) NOT NULL,
    addtax DECIMAL(19, 2) NOT NULL,
    total_amt DECIMAL(19, 2) NOT NULL,
    FOREIGN KEY (estimate_id) REFERENCES sales_scm.sbl_estimate(estimate_id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_demand_cust_cd ON sales_scm.sbl_demand(cust_cd);
CREATE INDEX idx_demand_stnd_dt ON sales_scm.sbl_demand(demand_stnd_dt);
CREATE INDEX idx_demand_slstmt_no ON sales_scm.sbl_demand(slstmt_no);
CREATE INDEX idx_estimate_doc_type ON sales_scm.sbl_estimate(doc_type);
CREATE INDEX idx_estimate_reg_dt ON sales_scm.sbl_estimate(reg_dt);
CREATE INDEX idx_estimate_item_estimate_id ON sales_scm.sbl_estimate_item(estimate_id);
