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
VALUES('019dffbe-d07e-7bfd-ab68-f566727ec57e', 'SystemBatch', 'system.batch@resmenu.com',
    '$2a$10$eO6sAdt3WNQscoIA1f.RmuTNTF.ovZseDmr1jRBteIOMmDRSgknlG',
    true, true, true, false, now(), now()
);

INSERT INTO user_role(user_id, role_id)
VALUES
('019dffbe-d07e-7bfd-ab68-f566727ec57e', '019dffbe-d07e-7bfd-ab68-f566727ec57e');
