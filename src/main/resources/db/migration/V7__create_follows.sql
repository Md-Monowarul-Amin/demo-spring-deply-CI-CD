-- ============================================================
-- V7 — Follows table
-- Composite PK — no surrogate key needed.
-- A user cannot follow themselves (check constraint).
-- ============================================================

CREATE TABLE follows (
    follower_id  BIGINT      NOT NULL,
    following_id BIGINT      NOT NULL,
    created_at   TIMESTAMPTZ NOT NULL,

    CONSTRAINT pk_follows PRIMARY KEY (follower_id, following_id),
    CONSTRAINT fk_follows_follower FOREIGN KEY (follower_id)
        REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_follows_following FOREIGN KEY (following_id)
        REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT chk_follows_no_self_follow
        CHECK (follower_id <> following_id)
);

CREATE INDEX idx_follows_follower  ON follows (follower_id);
CREATE INDEX idx_follows_following ON follows (following_id);
