package com.example.demo.repository.impl;

import com.example.demo.entity.QLike;
import com.example.demo.entity.QUser;
import com.example.demo.entity.User;
import com.example.demo.enam.LikeTargetType;
import com.example.demo.repository.LikeRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepositoryCustom {

    private final JPAQueryFactory query;

    private static final QLike like = QLike.like;
    private static final QUser user = QUser.user;

    @Override
    public Page<User> findUserWhoLiked(Long targetId, LikeTargetType targetType, Pageable pageable) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(like.targetId.eq(targetId));
        predicate.and(like.targetType.eq(targetType));
        predicate.and(like.user.isActive.isTrue());

        List<User> users = query
                .select(like.user)
                .from(like)
                .join(like.user, user)
                .where(predicate)
                .orderBy(like.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = query
                .select(like.count())
                .from(like)
                .where(predicate)
                .fetchOne();

        return new PageImpl<>(users, pageable, total != null ? total : 0L);
    }

    @Override
    public long countByTarget(Long targetId, LikeTargetType targetType) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(like.targetId.eq(targetId));
        predicate.and(like.targetType.eq(targetType));

        Long count = query
                .select(like.count())
                .from(like)
                .where(predicate)
                .fetchOne();

        return count != null ? count : 0L;
    }
}