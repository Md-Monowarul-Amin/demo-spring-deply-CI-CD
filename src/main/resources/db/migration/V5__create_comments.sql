-- ============================================================
-- V5 — Comments table
-- Supports top-level comments (depth=0) and replies (depth=1).
-- parent_id is NULL for top-level comments.
-- ============================================================

CREATE TABLE comments (
    id          BIGINT      NOT NULL DEFAULT nextval('global_seq'),
    post_id     BIGINT      NOT NULL,
    author_id   BIGINT      NOT NULL,
    parent_id   BIGINT,
    content     TEXT        NOT NULL,
    depth       INT         NOT NULL DEFAULT 0,
    like_count  INT         NOT NULL DEFAULT 0,
    created_at  TIMESTAMPTZ NOT NULL,
    updated_at  TIMESTAMPTZ NOT NULL,
    created_by  BIGINT,
    updated_by  BIGINT,

    CONSTRAINT pk_comments PRIMARY KEY (id),
    CONSTRAINT fk_comments_post FOREIGN KEY (post_id)
        REFERENCES posts (id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_author FOREIGN KEY (author_id)
        REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_parent FOREIGN KEY (parent_id)
        REFERENCES comments (id) ON DELETE CASCADE,
    CONSTRAINT chk_comments_depth CHECK (depth IN (0, 1))
);

CREATE INDEX idx_comments_post_created ON comments (post_id, created_at DESC);
CREATE INDEX idx_comments_parent_id    ON comments (parent_id);
