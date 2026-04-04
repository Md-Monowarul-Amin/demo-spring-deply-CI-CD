-- ============================================================
-- V1 — Sequences
-- allocationSize in entities = 50, so increment by 50 here
-- to keep Hibernate's in-memory pool in sync with Postgres.
-- ============================================================

CREATE SEQUENCE IF NOT EXISTS global_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 50;

CREATE SEQUENCE IF NOT EXISTS like_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 50;

CREATE SEQUENCE IF NOT EXISTS post_media_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 50;

CREATE SEQUENCE IF NOT EXISTS refresh_token_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 50;
