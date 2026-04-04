-- ============================================================
-- V6 — Likes table
-- Polymorphic: covers both post and comment likes via
-- target_id + target_type. One row per user per target.
-- ============================================================

CREATE TYPE like_target_type AS ENUM ('POST', 'COMMENT');

CREATE TABLE likes (
    id          BIGINT           NOT NULL DEFAULT nextval('like_seq'),
    user_id     BIGINT           NOT NULL,
    target_id   BIGINT           NOT NULL,
    target_type like_target_type NOT NULL,
    created_at  TIMESTAMPTZ      NOT NULL,

    CONSTRAINT pk_likes PRIMARY KEY (id),
    CONSTRAINT uq_likes_user_target UNIQUE (user_id, target_id, target_type),
    CONSTRAINT fk_likes_user FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_likes_target      ON likes (target_id, target_type);
CREATE INDEX idx_likes_user_target ON likes (user_id, target_id, target_type);
