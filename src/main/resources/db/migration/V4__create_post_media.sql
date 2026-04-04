-- ============================================================
-- V4 — Post media table
-- ============================================================

CREATE TYPE media_type AS ENUM ('IMAGE', 'VIDEO');

CREATE TABLE post_media (
    id          BIGINT      NOT NULL DEFAULT nextval('post_media_seq'),
    post_id     BIGINT      NOT NULL,
    s3_key      TEXT        NOT NULL,
    media_type  media_type  NOT NULL,
    sort_order  INT         NOT NULL DEFAULT 0,
    created_at  TIMESTAMPTZ NOT NULL,

    CONSTRAINT pk_post_media PRIMARY KEY (id),
    CONSTRAINT fk_post_media_post FOREIGN KEY (post_id)
        REFERENCES posts (id) ON DELETE CASCADE
);

CREATE INDEX idx_post_media_post_id ON post_media (post_id);
