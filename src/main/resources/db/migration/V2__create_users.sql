-- ============================================================
-- V2 — Users table
-- ============================================================

CREATE TABLE users (
    id          BIGINT          NOT NULL DEFAULT nextval('global_seq'),
    first_name  VARCHAR(100)    NOT NULL,
    last_name   VARCHAR(100)    NOT NULL,
    email       VARCHAR(255)    NOT NULL,
    password_hash TEXT          NOT NULL,
    avatar_url  TEXT,
    is_active   BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ     NOT NULL,
    updated_at  TIMESTAMPTZ     NOT NULL,
    created_by  BIGINT,
    updated_by  BIGINT,

    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE INDEX idx_users_email ON users (email);
