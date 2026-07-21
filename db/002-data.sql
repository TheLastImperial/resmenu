INSERT INTO roles(id, name, created_at, updated_at)
VALUES
('019dffbe-d07e-7bfd-ab68-f566727ec57a', 'ROLE_ROOT', NOW(), NOW()),
('019dffbe-d07e-7bfd-ab68-f566727ec57b', 'ROLE_ADMIN', NOW(), NOW()),
('019dffbe-d07e-7bfd-ab68-f566727ec57c', 'ROLE_USER', NOW(), NOW()),
('019dffbe-d07e-7bfd-ab68-f566727ec57d', 'ROLE_MONITOR', NOW(), NOW());

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
VALUES('019dffbe-d07e-7bfd-ab68-f566727ec57d', 'user', 'user@email.com',
    '$2a$10$eO6sAdt3WNQscoIA1f.RmuTNTF.ovZseDmr1jRBteIOMmDRSgknlG',
    true, true, true, true, now(), now()
),
--- Password: prometheus
(
    '019dffbe-d07e-7bfd-ab68-f566727ec57c', 'prometheus', 'prometheus@resmenu.com',
    '$2a$10$cBedNJzkjMWO2lKpj6M3y.xOVau4yqISTUsgM2RKos01EEnqx1RZK', true, true, true, true, NOW(),
    NOW()
);

INSERT INTO user_settings(
    id, account_expired_at, created_at, credentials_expired_at, updated_at, user_id
)
VALUES(
    '119dffbe-d07e-7bfd-ab68-f566727ec57d', '20260505', NOW(), NULL, NOW(),
    '019dffbe-d07e-7bfd-ab68-f566727ec57d'
);


INSERT INTO user_role(user_id, role_id)
VALUES
('019dffbe-d07e-7bfd-ab68-f566727ec57d', '019dffbe-d07e-7bfd-ab68-f566727ec57a'),
('019dffbe-d07e-7bfd-ab68-f566727ec57d', '019dffbe-d07e-7bfd-ab68-f566727ec57b'),
('019dffbe-d07e-7bfd-ab68-f566727ec57d', '019dffbe-d07e-7bfd-ab68-f566727ec57c'),
('019dffbe-d07e-7bfd-ab68-f566727ec57c', '019dffbe-d07e-7bfd-ab68-f566727ec57d');

--------- Menus ------------
INSERT INTO public.menus
VALUES (
    1, false, 'Address', '2026-07-17 17:52:35.266249', 'Sushie', '1234',
    '2026-07-17 17:52:35.266317', '019dffbe-d07e-7bfd-ab68-f566727ec57d'
);
--------- Sections ---------
INSERT INTO public.sections
VALUES (1, '2026-07-17 17:52:45.220145', 'Entradas', '2026-07-17 17:52:45.220186', 1);
INSERT INTO public.sections
VALUES (2, '2026-07-17 17:52:59.461582', 'Empanizados', '2026-07-17 17:52:59.461602', 1);
INSERT INTO public.sections
VALUES (3, '2026-07-17 17:53:10.555135', 'Postres', '2026-07-17 17:53:10.555166', 1);
INSERT INTO public.sections
VALUES (4, '2026-07-17 17:53:16.11691', 'Bebidas', '2026-07-17 17:53:16.116946', 1);
---------- Products --------
INSERT INTO public.products
VALUES (
    1, '2026-07-17 17:54:46.908291', 'Vainas de soya tiernas hervidas al vapor.', 'Edamanes', 80,
    '2026-07-17 17:54:46.908324', 1, 1
);
INSERT INTO public.products
VALUES (
    2, '2026-07-17 17:55:30.710348',
    'Sushie empanizado con camaron, res, queso Filadelfia, aguacate y pepino.', 'Mar y Tierra', 140,
    '2026-07-17 17:55:30.710384', 1, 2
);
INSERT INTO public.products
VALUES (
    3, '2026-07-17 17:55:49.835794', 'Delicioso flan de vainilla', 'Flan', 110,
    '2026-07-17 17:55:49.835827', 1, 3
);
INSERT INTO public.products
VALUES (
    4, '2026-07-17 17:56:06.650712', 'Te de refill', 'Te', 45, '2026-07-17 17:56:06.650742', 1, 4
);

---------- Next id value ------------
SELECT pg_catalog.setval('public.menus_id_seq', 1, true);
SELECT pg_catalog.setval('public.sections_id_seq', 4, true);
SELECT pg_catalog.setval('public.products_id_seq', 4, true);
