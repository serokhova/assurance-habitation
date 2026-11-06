-- ============================================================
-- Initial Seed Data
-- Domain: Home Insurance
-- ============================================================

-- 1. Standard Coverages
INSERT INTO assurance.coverage (code, description, base_price) VALUES
('RC', 'Personal and tenant third-party liability', 15.00),
('FIRE', 'Fire, explosion, smoke and lightning damage', 35.50),
('WATER', 'Water damage, pipe bursts and plumbing leaks', 25.00),
('THEFT', 'Theft, attempted burglary and vandalism', 40.00),
('GLASS', 'Broken glass, mirrors, windows and verandas', 18.00),
('NAT_CAT', 'Natural disasters, storms and floods', 12.00)
ON CONFLICT DO NOTHING;

-- 2. Sample Policyholders (Users)
INSERT INTO assurance."user" (name, email, phone_number, created_at) VALUES
('Alice Morel', 'alice.morel@email.fr', '+33611223344', CURRENT_TIMESTAMP - INTERVAL '30 days'),
('Thomas Bernard', 'thomas.bernard@email.fr', '+33622334455', CURRENT_TIMESTAMP - INTERVAL '20 days'),
('Camille Dubois', 'camille.dubois@email.fr', '+33633445566', CURRENT_TIMESTAMP - INTERVAL '10 days')
ON CONFLICT (email) DO NOTHING;

-- 3. Sample Accommodations
INSERT INTO assurance.accommodation (street, city, country, postal_code, type, surface_area, room_count, user_id, created_at)
SELECT '15 Rue de la Paix', 'Paris', 'France', '75002', 'appartment', 65, 3, id, CURRENT_TIMESTAMP - INTERVAL '25 days'
FROM assurance."user" WHERE email = 'alice.morel@email.fr'
LIMIT 1;

INSERT INTO assurance.accommodation (street, city, country, postal_code, type, surface_area, room_count, user_id, created_at)
SELECT '8 Avenue des Champs', 'Lyon', 'France', '69006', 'house', 130, 5, id, CURRENT_TIMESTAMP - INTERVAL '18 days'
FROM assurance."user" WHERE email = 'thomas.bernard@email.fr'
LIMIT 1;

-- 4. Sample Contracts
INSERT INTO assurance.contract (start_date, end_date, formula, accommodation_id, created_at)
SELECT CURRENT_DATE - INTERVAL '6 months', CURRENT_DATE + INTERVAL '6 months', 'standard', a.id, CURRENT_TIMESTAMP - INTERVAL '20 days'
FROM assurance.accommodation a
JOIN assurance."user" u ON a.user_id = u.id
WHERE u.email = 'alice.morel@email.fr'
LIMIT 1;

INSERT INTO assurance.contract (start_date, end_date, formula, accommodation_id, created_at)
SELECT CURRENT_DATE - INTERVAL '3 months', CURRENT_DATE + INTERVAL '9 months', 'premium', a.id, CURRENT_TIMESTAMP - INTERVAL '15 days'
FROM assurance.accommodation a
JOIN assurance."user" u ON a.user_id = u.id
WHERE u.email = 'thomas.bernard@email.fr'
LIMIT 1;

-- 5. Contract Coverages associations (contract_coverage)
-- Alice's standard contract (RC, WATER, FIRE, GLASS)
INSERT INTO assurance.contract_coverage (contract_id, coverage_id)
SELECT c.id, cov.id
FROM assurance.contract c
JOIN assurance.accommodation a ON c.accommodation_id = a.id
JOIN assurance."user" u ON a.user_id = u.id
CROSS JOIN assurance.coverage cov
WHERE u.email = 'alice.morel@email.fr' AND cov.code IN ('RC', 'WATER', 'FIRE', 'GLASS')
ON CONFLICT DO NOTHING;

-- Thomas's premium contract (all coverages)
INSERT INTO assurance.contract_coverage (contract_id, coverage_id)
SELECT c.id, cov.id
FROM assurance.contract c
JOIN assurance.accommodation a ON c.accommodation_id = a.id
JOIN assurance."user" u ON a.user_id = u.id
CROSS JOIN assurance.coverage cov
WHERE u.email = 'thomas.bernard@email.fr'
ON CONFLICT DO NOTHING;

-- 6. Sample Claim
INSERT INTO assurance.claim (user_id, contract_id, coverage_id, incident_date, description, created_at)
SELECT u.id, c.id, cov.id, CURRENT_DATE - INTERVAL '15 days', 'Water pipe leak under the kitchen sink causing floor infiltration.', CURRENT_TIMESTAMP - INTERVAL '14 days'
FROM assurance."user" u
JOIN assurance.accommodation a ON a.user_id = u.id
JOIN assurance.contract c ON c.accommodation_id = a.id
JOIN assurance.coverage cov ON cov.code = 'WATER'
WHERE u.email = 'alice.morel@email.fr'
LIMIT 1;
