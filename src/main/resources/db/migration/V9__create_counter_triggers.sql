-- ============================================================
-- V9 — Counter triggers
-- Keeps like_count / comment_count in sync automatically
-- at the DB level — a safety net alongside application events.
-- ============================================================

-- ── Post like_count ─────────────────────────────────────────

CREATE OR REPLACE FUNCTION fn_update_post_like_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' AND NEW.target_type = 'POST' THEN
        UPDATE posts SET like_count = like_count + 1 WHERE id = NEW.target_id;
    ELSIF TG_OP = 'DELETE' AND OLD.target_type = 'POST' THEN
        UPDATE posts SET like_count = GREATEST(like_count - 1, 0) WHERE id = OLD.target_id;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_post_like_count
AFTER INSERT OR DELETE ON likes
FOR EACH ROW EXECUTE FUNCTION fn_update_post_like_count();

-- ── Comment like_count ───────────────────────────────────────

CREATE OR REPLACE FUNCTION fn_update_comment_like_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' AND NEW.target_type = 'COMMENT' THEN
        UPDATE comments SET like_count = like_count + 1 WHERE id = NEW.target_id;
    ELSIF TG_OP = 'DELETE' AND OLD.target_type = 'COMMENT' THEN
        UPDATE comments SET like_count = GREATEST(like_count - 1, 0) WHERE id = OLD.target_id;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_comment_like_count
AFTER INSERT OR DELETE ON likes
FOR EACH ROW EXECUTE FUNCTION fn_update_comment_like_count();

-- ── Post comment_count ───────────────────────────────────────

CREATE OR REPLACE FUNCTION fn_update_post_comment_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE posts SET comment_count = comment_count + 1 WHERE id = NEW.post_id;
    ELSIF TG_OP = 'DELETE' THEN
        UPDATE posts SET comment_count = GREATEST(comment_count - 1, 0) WHERE id = OLD.post_id;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_post_comment_count
AFTER INSERT OR DELETE ON comments
FOR EACH ROW EXECUTE FUNCTION fn_update_post_comment_count();
