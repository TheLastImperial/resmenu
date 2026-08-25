
-- Password: 1234
INSERT INTO users (
    id,
    username,
    email,
    password,
    account_non_expired,
    account_non_locked,
    credentials_non_expired,
    enabled,
    created_at,
    updated_at
)
VALUES('019dffbe-d07e-7bfd-ab68-f566727ec580', 'batchTest1', 'batchTest1@email.com',
    '$2a$10$eO6sAdt3WNQscoIA1f.RmuTNTF.ovZseDmr1jRBteIOMmDRSgknlG',
    true, true, true, true, now(), now()
),
('019dffbe-d07e-7bfd-ab68-f566727ec581', 'batchTest2', 'batchTest2@email.com',
    '$2a$10$eO6sAdt3WNQscoIA1f.RmuTNTF.ovZseDmr1jRBteIOMmDRSgknlG',
    true, true, true, true, now(), now()
),
('019dffbe-d07e-7bfd-ab68-f566727ec582', 'batchTest3', 'batchTest3@email.com',
    '$2a$10$eO6sAdt3WNQscoIA1f.RmuTNTF.ovZseDmr1jRBteIOMmDRSgknlG',
    false, true, true, true, now(), now()
),
('019dffbe-d07e-7bfd-ab68-f566727ec583', 'batchTest4', 'batchTest4@email.com',
    '$2a$10$eO6sAdt3WNQscoIA1f.RmuTNTF.ovZseDmr1jRBteIOMmDRSgknlG',
    true, true, false, true, now(), now()
);
;

INSERT INTO user_settings(
    id, account_expired_at, created_at, credentials_expired_at, updated_at, user_id
)
VALUES(
    '119dffbe-d07e-7bfd-ab68-f566727ec580', '20200101', NOW(), NULL, NOW(),
    '019dffbe-d07e-7bfd-ab68-f566727ec580'
),
(
    '119dffbe-d07e-7bfd-ab68-f566727ec581', NULL, NOW(), '20200101', NOW(),
    '019dffbe-d07e-7bfd-ab68-f566727ec581'
),
(
    '119dffbe-d07e-7bfd-ab68-f566727ec582', '20200101', NOW(), NULL, NOW(),
    '019dffbe-d07e-7bfd-ab68-f566727ec582'
),
(
    '119dffbe-d07e-7bfd-ab68-f566727ec583', NULL, NOW(), '20200101', NOW(),
    '019dffbe-d07e-7bfd-ab68-f566727ec583'
);
