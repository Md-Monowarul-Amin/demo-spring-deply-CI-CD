-- ============================================================
-- V3 — Posts table
-- ============================================================

CREATE TYPE visibility_type AS ENUM ('PUBLIC', 'PRIVATE');

CREATE TABLE posts (
    id            BIGINT          NOT NULL DEFAULT nextval('global_seq'),
    author_id     BIGINT          NOT NULL,
    content       TEXT,
    visibility    visibility_type NOT NULL DEFAULT 'PUBLIC',
    like_count    INT             NOT NULL DEFAULT 0,
    comment_count INT             NOT NULL DEFAULT 0,
    created_at    TIMESTAMPTZ     NOT NULL,
    updated_at    TIMESTAMPTZ     NOT NULL,
    created_by    BIGINT,
    updated_by    BIGINT,

    CONSTRAINT pk_posts PRIMARY KEY (id),
    CONSTRAINT fk_posts_author FOREIGN KEY (author_id)
        REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_posts_author_created     ON posts (author_id, created_at DESC);
CREATE INDEX idx_posts_visibility_created ON posts (visibility, created_at DESC);
