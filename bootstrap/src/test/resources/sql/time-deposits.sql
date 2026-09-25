TRUNCATE withdrawals, time_deposits;

INSERT INTO time_deposits (id, plan_type, days, balance) VALUES
                                                             (1, 'basic',    45, 1000.00),  -- 1000 * 0.01 / 12 = 0.83
                                                             (2, 'basic',    20, 1000.00),  -- days <= 30: no interest
                                                             (3, 'student', 200, 2000.00),  -- 2000 * 0.03 / 12 = 5.00
                                                             (4, 'student', 400, 2000.00),  -- days >= 366: no interest
                                                             (5, 'premium',  40, 1200.00),  -- days <= 45: no interest
                                                             (6, 'premium',  60, 1200.00);  -- 1200 * 0.05 / 12 = 5.00

INSERT INTO withdrawals (id, time_deposit_id, amount, date) VALUES
    (1, 1, 50.00, '2026-01-15');
