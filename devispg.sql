-- Création des types ENUM
DROP TYPE IF EXISTS accommodation_type CASCADE;
CREATE TYPE accommodation_type AS ENUM ('appartment', 'house');

DROP TYPE IF EXISTS contract_formula CASCADE;
CREATE TYPE contract_formula AS ENUM ('basic', 'standard', 'premium');

-- --------------------------------------------------------
-- Table: "user" ("user" est un mot réservé en PostgreSQL)
-- --------------------------------------------------------
DROP TABLE IF EXISTS "user" CASCADE;
CREATE TABLE "user" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(80) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- --------------------------------------------------------
-- Table: accommodation
-- --------------------------------------------------------
DROP TABLE IF EXISTS accommodation CASCADE;
CREATE TABLE accommodation (
    id SERIAL PRIMARY KEY,
    street VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    postal_code VARCHAR(100) NOT NULL,
    type accommodation_type NOT NULL,
    surface_area INT NOT NULL,
    room_count INT NOT NULL,
    user_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES "user" (id)
);

CREATE INDEX idx_accommodation_user_id ON accommodation (user_id);

-- --------------------------------------------------------
-- Table: coverage
-- --------------------------------------------------------
DROP TABLE IF EXISTS coverage CASCADE;
CREATE TABLE coverage (
    id SERIAL PRIMARY KEY,
    code VARCHAR(30) NOT NULL,
    description TEXT NOT NULL,
    base_price DECIMAL(10,2) NOT NULL
);

-- --------------------------------------------------------
-- Table: contract
-- --------------------------------------------------------
DROP TABLE IF EXISTS contract CASCADE;
CREATE TABLE contract (
    id SERIAL PRIMARY KEY,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    formula contract_formula NOT NULL,
    accommodation_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_acc_id FOREIGN KEY (accommodation_id) REFERENCES accommodation (id)
);

CREATE INDEX idx_contract_accommodation_id ON contract (accommodation_id);

-- --------------------------------------------------------
-- Table: contract_coverage
-- --------------------------------------------------------
DROP TABLE IF EXISTS contract_coverage CASCADE;
CREATE TABLE contract_coverage (
    contract_id INT NOT NULL,
    coverage_id INT NOT NULL,
    PRIMARY KEY (contract_id, coverage_id),
    CONSTRAINT contract_coverage_fk_1 FOREIGN KEY (contract_id) REFERENCES contract (id) ON DELETE CASCADE,
    CONSTRAINT contract_coverage_fk_2 FOREIGN KEY (coverage_id) REFERENCES coverage (id)
);

CREATE INDEX idx_contract_coverage_coverage_id ON contract_coverage (coverage_id);

-- --------------------------------------------------------
-- Table: claim
-- --------------------------------------------------------
DROP TABLE IF EXISTS claim CASCADE;
CREATE TABLE claim (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    contract_id INT NOT NULL,
    coverage_id INT NOT NULL,
    description TEXT,
    incident_date DATE NOT NULL,
    CONSTRAINT fk_contract FOREIGN KEY (contract_id) REFERENCES contract (id),
    CONSTRAINT fk_coverage FOREIGN KEY (coverage_id) REFERENCES coverage (id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES "user" (id)
);

CREATE INDEX idx_claim_contract_id ON claim (contract_id);
CREATE INDEX idx_claim_user_id ON claim (user_id);
CREATE INDEX idx_claim_coverage_id ON claim (coverage_id);

-- --------------------------------------------------------
-- Table: notification
-- --------------------------------------------------------
DROP TABLE IF EXISTS notification CASCADE;
CREATE TABLE notification (
    id SERIAL PRIMARY KEY,
    msg_body TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    claim_id INT NOT NULL,
    user_id INT NOT NULL,
    CONSTRAINT fk_claim_notif FOREIGN KEY (claim_id) REFERENCES claim (id),
    CONSTRAINT fk_user_notification FOREIGN KEY (user_id) REFERENCES "user" (id)
);

CREATE INDEX idx_notification_claim_id ON notification (claim_id);
CREATE INDEX idx_notification_user_id ON notification (user_id);

-- --------------------------------------------------------
-- Table: log
-- --------------------------------------------------------
DROP TABLE IF EXISTS log CASCADE;
CREATE TABLE log (
    id SERIAL PRIMARY KEY,
    event_type VARCHAR(150) NOT NULL,
    payload JSONB NOT NULL,
    event_time TIMESTAMP NOT NULL,
    consumer_received_at TIMESTAMP NOT NULL
);