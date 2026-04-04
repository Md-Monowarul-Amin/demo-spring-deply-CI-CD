package com.example.demo.repository.impl;

import com.example.demo.entity.QFollow;
import com.example.demo.entity.QPost;
import com.example.demo.entity.Post;
import com.example.demo.enam.Visibility;
import com.example.demo.repository.PostRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
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
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory query;

    private static final QPost post     = QPost.post;
    private static final QFollow follow = QFollow.follow;

    @Override
    public Page<Post> findPublicPosts(Pageable pageable) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(post.visibility.eq(Visibility.PUBLIC));
        predicate.and(post.author.isActive.isTrue());

        return fetchPage(predicate, pageable);
    }

    @Override
    public Page<Post> findPostsByAuthor(Long authorId, Long viewerId, Pageable pageable) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(post.author.id.eq(authorId));

        if (!authorId.equals(viewerId)) {
            // Viewer is not the author — show PUBLIC posts only
            predicate.and(post.visibility.eq(Visibility.PUBLIC));
        }
        // Author viewing their own profile — no visibility filter, see everything

        return fetchPage(predicate, pageable);
    }

    @Override
    public Page<Post> findFeedForUser(Long viewerId, Pageable pageable) {
        BooleanBuilder predicate = new BooleanBuilder();

        // Posts from users the viewer follows
        predicate.and(post.author.id.in(
                JPAExpressions
                        .select(follow.following.id)
                        .from(follow)
                        .where(follow.follower.id.eq(viewerId))
        ));

        // Only PUBLIC posts from followed users
        predicate.and(post.visibility.eq(Visibility.PUBLIC));
        predicate.and(post.author.isActive.isTrue());

        return fetchPage(predicate, pageable);
    }

    // ── Shared helpers ───────────────────────────────────────────────────────

    private Page<Post> fetchPage(BooleanBuilder predicate, Pageable pageable) {
        List<Post> results = buildBaseQuery(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = buildCountQuery(predicate);

        return new PageImpl<>(results, pageable, total);
    }

    private JPAQuery<Post> buildBaseQuery(BooleanBuilder predicate) {
        return query
                .selectFrom(post)
                .where(predicate)
                .orderBy(post.createdAt.desc());
    }

    private long buildCountQuery(BooleanBuilder predicate) {
        Long count = query
                .select(post.count())
                .from(post)
                .where(predicate)
                .fetchOne();
        return count != null ? count : 0L;
    }
}