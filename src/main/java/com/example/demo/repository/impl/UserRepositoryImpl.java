package com.example.demo.repository.impl;

import com.example.demo.entity.QFollow;
import com.example.demo.entity.QUser;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory query;

    private static final QUser   user   = QUser.user;
    private static final QFollow follow = QFollow.follow;

    @Override
    public List<User> searchUsers(String keyword, int limit) {
        String pattern = "%" + keyword.toLowerCase() + "%";

        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(user.isActive.isTrue());
        predicate.and(
                user.firstName.lower().like(pattern)
                        .or(user.lastName.lower().like(pattern))
                        .or(user.email.lower().like(pattern))
        );

        return query
                .selectFrom(user)
                .where(predicate)
                .orderBy(user.firstName.asc())
                .limit(limit)
                .fetch();
    }

    @Override
    public boolean isFollowing(Long followerId, Long followingId) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(follow.follower.id.eq(followerId));
        predicate.and(follow.following.id.eq(followingId));

        return query
                .selectOne()
                .from(follow)
                .where(predicate)
                .fetchFirst() != null;
    }

    @Override
    public Optional<User> findByIdWithStats(Long userId) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(user.id.eq(userId));
        predicate.and(user.isActive.isTrue());

        return Optional.ofNullable(
                query
                        .selectFrom(user)
                        .where(predicate)
                        .fetchOne()
        );
    }
}