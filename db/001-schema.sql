CREATE TABLE roles (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    name character varying(255),
    updated_at timestamp(6) without time zone,
    PRIMARY KEY(id)
);

CREATE TABLE users (
    id uuid NOT NULL,
    account_non_expired boolean NOT NULL,
    account_non_locked boolean NOT NULL,
    created_at timestamp(6) without time zone,
    credentials_non_expired boolean NOT NULL,
    email character varying(255),
    enabled boolean NOT NULL,
    password character varying(255),
    updated_at timestamp(6) without time zone,
    username character varying(255),
    PRIMARY KEY(id)
);

CREATE TABLE user_role (
    user_id uuid NOT NULL,
    role_id uuid NOT NULL
);
