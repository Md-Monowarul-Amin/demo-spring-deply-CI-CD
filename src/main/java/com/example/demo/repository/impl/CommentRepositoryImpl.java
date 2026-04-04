package com.example.demo.repository.impl;

import com.example.demo.entity.Comment;
import com.example.demo.entity.QComment;
import com.example.demo.repository.CommentRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory query;

    private static final QComment comment = QComment.comment;

    @Override
    public Page<Comment> findTopLevelCommentsByPost(Long postId, Pageable pageable) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(comment.post.id.eq(postId));
        predicate.and(comment.depth.eq(0));
        predicate.and(comment.parent.isNull());

        return fetchPage(predicate, pageable);
    }

    @Override
    public Page<Comment> findRepliesByParent(Long parentCommentId, Pageable pageable) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(comment.parent.id.eq(parentCommentId));
        predicate.and(comment.depth.eq(1));

        return fetchPage(predicate, pageable);
    }

    // ── Shared helpers ───────────────────────────────────────────────────────

    private Page<Comment> fetchPage(BooleanBuilder predicate, Pageable pageable) {
        List<Comment> results = buildBaseQuery(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = buildCountQuery(predicate);

        return new PageImpl<>(results, pageable, total);
    }

    private JPAQuery<Comment> buildBaseQuery(BooleanBuilder predicate) {
        return query
                .selectFrom(comment)
                .where(predicate)
                .orderBy(comment.createdAt.desc());
    }

    private long buildCountQuery(BooleanBuilder predicate) {
        Long count = query
                .select(comment.count())
                .from(comment)
                .where(predicate)
                .fetchOne();
        return count != null ? count : 0L;
    }
}