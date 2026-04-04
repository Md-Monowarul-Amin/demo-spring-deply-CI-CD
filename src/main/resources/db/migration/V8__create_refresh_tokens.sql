-- ============================================================
-- V8 — Refresh tokens table
-- token column stores a HASHED value, never the raw JWT.
-- Expired/revoked tokens should be cleaned up by a
-- scheduled job to keep this table lean.
-- ============================================================

CREATE TABLE refresh_tokens (
    id          BIGINT      NOT NULL DEFAULT nextval('refresh_token_seq'),
    user_id     BIGINT      NOT NULL,
    token       TEXT        NOT NULL,
    expires_at  TIMESTAMPTZ NOT NULL,
    revoked     BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMPTZ NOT NULL,

    CONSTRAINT pk_refresh_tokens PRIMARY KEY (id),
    CONSTRAINT uq_refresh_tokens_token UNIQUE (token),
    CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_refresh_tokens_user  ON refresh_tokens (user_id);
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens (token);
