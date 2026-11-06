-- ============================================================
-- Schéma 1 : assurance (domaine métier principal)
-- ============================================================
CREATE SCHEMA IF NOT EXISTS assurance;

-- Création des types ENUM
DROP TYPE IF EXISTS assurance.accommodation_type CASCADE;
CREATE TYPE assurance.accommodation_type AS ENUM ('appartment', 'house');

DROP TYPE IF EXISTS assurance.contract_formula CASCADE;
CREATE TYPE assurance.contract_formula AS ENUM ('basic', 'standard', 'premium');

-- --------------------------------------------------------
-- Table: assurance.user ("user" est un mot réservé en PostgreSQL)
-- --------------------------------------------------------
DROP TABLE IF EXISTS assurance."user" CASCADE;
CREATE TABLE assurance."user" (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(254) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- --------------------------------------------------------
-- Table: assurance.accommodation
-- --------------------------------------------------------
DROP TABLE IF EXISTS assurance.accommodation CASCADE;
CREATE TABLE assurance.accommodation (
    id BIGSERIAL PRIMARY KEY,
    street VARCHAR(150) NOT NULL,
    city VARCHAR(100) NOT NULL,
    country VARCHAR(60) NOT NULL,
    postal_code VARCHAR(10) NOT NULL,
    type assurance.accommodation_type NOT NULL,
    surface_area INT NOT NULL,
    room_count INT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES assurance."user" (id)
);

CREATE INDEX idx_accommodation_user_id ON assurance.accommodation (user_id);

-- --------------------------------------------------------
-- Table: assurance.coverage
-- --------------------------------------------------------
DROP TABLE IF EXISTS assurance.coverage CASCADE;
CREATE TABLE assurance.coverage (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(30) NOT NULL,
    description TEXT NOT NULL,
    base_price DECIMAL(10,2) NOT NULL
);

-- --------------------------------------------------------
-- Table: assurance.contract
-- --------------------------------------------------------
DROP TABLE IF EXISTS assurance.contract CASCADE;
CREATE TABLE assurance.contract (
    id BIGSERIAL PRIMARY KEY,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    formula assurance.contract_formula NOT NULL,
    accommodation_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_acc_id FOREIGN KEY (accommodation_id) REFERENCES assurance.accommodation (id)
);

CREATE INDEX idx_contract_accommodation_id ON assurance.contract (accommodation_id);

-- --------------------------------------------------------
-- Table: assurance.contract_coverage
-- --------------------------------------------------------
DROP TABLE IF EXISTS assurance.contract_coverage CASCADE;
CREATE TABLE assurance.contract_coverage (
    contract_id BIGINT NOT NULL,
    coverage_id BIGINT NOT NULL,
    PRIMARY KEY (contract_id, coverage_id),
    CONSTRAINT contract_coverage_fk_1 FOREIGN KEY (contract_id) REFERENCES assurance.contract (id) ON DELETE CASCADE,
    CONSTRAINT contract_coverage_fk_2 FOREIGN KEY (coverage_id) REFERENCES assurance.coverage (id)
);

CREATE INDEX idx_contract_coverage_coverage_id ON assurance.contract_coverage (coverage_id);

-- --------------------------------------------------------
-- Table: assurance.claim
-- --------------------------------------------------------
DROP TABLE IF EXISTS assurance.claim CASCADE;
CREATE TABLE assurance.claim (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    contract_id BIGINT NOT NULL,
    coverage_id BIGINT NOT NULL,
    description TEXT,
    incident_date DATE NOT NULL,
    CONSTRAINT fk_contract FOREIGN KEY (contract_id) REFERENCES assurance.contract (id),
    CONSTRAINT fk_coverage FOREIGN KEY (coverage_id) REFERENCES assurance.coverage (id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES assurance."user" (id)
);

CREATE INDEX idx_claim_contract_id ON assurance.claim (contract_id);
CREATE INDEX idx_claim_user_id ON assurance.claim (user_id);
CREATE INDEX idx_claim_coverage_id ON assurance.claim (coverage_id);

-- ============================================================
-- Schéma 2 : messaging (consommateur JMS)
-- ============================================================
CREATE SCHEMA IF NOT EXISTS messaging;

-- --------------------------------------------------------
-- Table: messaging.event
-- --------------------------------------------------------
DROP TABLE IF EXISTS messaging.event CASCADE;
CREATE TABLE messaging.event (
    id BIGSERIAL PRIMARY KEY,
    event_type VARCHAR(50) NOT NULL,
    payload TEXT NOT NULL,
    event_time TIMESTAMP NOT NULL,
    consumer_received_at TIMESTAMP NOT NULL
);
