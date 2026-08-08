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
VALUES('029dffbe-d07e-7bfd-ab68-f566727ec57e', 'newpassword', 'newpassword@email.com',
    '$2a$10$eO6sAdt3WNQscoIA1f.RmuTNTF.ovZseDmr1jRBteIOMmDRSgknlG',
    true, true, true, true, now(), now()
);

INSERT INTO user_role(user_id, role_id)
VALUES
('029dffbe-d07e-7bfd-ab68-f566727ec57e', '019dffbe-d07e-7bfd-ab68-f566727ec57c');

INSERT INTO user_recoveries
VALUES(
    '2930b83d-354f-4d6e-b5e2-9ff7e01fce24', NOW(), 'f','', NOW(), NOW() - interval '60 minutes',
    '029dffbe-d07e-7bfd-ab68-f566727ec57e'
),
(
    '2930b83d-354f-4d6e-b5e2-9ff7e01fce25', NOW(), 'f','', NOW(), NOW() + interval '10 minutes',
    '029dffbe-d07e-7bfd-ab68-f566727ec57e'
);
