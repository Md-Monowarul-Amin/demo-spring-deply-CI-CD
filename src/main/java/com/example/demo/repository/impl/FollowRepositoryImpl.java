package com.example.demo.repository.impl;

import com.example.demo.entity.QFollow;
import com.example.demo.entity.QUser;
import com.example.demo.entity.User;
import com.example.demo.repository.FollowRepositoryCustom;
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
public class FollowRepositoryImpl implements FollowRepositoryCustom {

    private final JPAQueryFactory query;

    private static final QFollow follow = QFollow.follow;
    private static final QUser   user   = QUser.user;

    @Override
    public Page<User> findFollowing(Long userId, Pageable pageable) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(follow.follower.id.eq(userId));
        predicate.and(follow.following.isActive.isTrue());

        List<User> results = query
                .select(follow.following)
                .from(follow)
                .join(follow.following, user)
                .where(predicate)
                .orderBy(follow.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = query
                .select(follow.count())
                .from(follow)
                .where(predicate)
                .fetchOne();

        return new PageImpl<>(results, pageable, total != null ? total : 0L);
    }

    @Override
    public Page<User> findFollowers(Long userId, Pageable pageable) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(follow.following.id.eq(userId));
        predicate.and(follow.follower.isActive.isTrue());

        List<User> results = query
                .select(follow.follower)
                .from(follow)
                .join(follow.follower, user)
                .where(predicate)
                .orderBy(follow.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = query
                .select(follow.count())
                .from(follow)
                .where(predicate)
                .fetchOne();

        return new PageImpl<>(results, pageable, total != null ? total : 0L);
    }
}