INSERT INTO time_deposits (id, plan_type, days, balance) VALUES
    (1, 'basic',   45,  1000.00),
    (2, 'student', 400, 2000.00),
    (3, 'premium', 60,  1200.00);

-- Inserted out of date order on purpose: the query must sort withdrawals by date.
INSERT INTO withdrawals (id, time_deposit_id, amount, date) VALUES
    (1, 1, 50.00,  '2026-02-10'),
    (2, 1, 100.00, '2026-01-15');
