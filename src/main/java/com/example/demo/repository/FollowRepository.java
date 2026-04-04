package com.example.demo.repository;

import com.example.demo.entity.Follow;
import com.example.demo.entity.FollowId;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface FollowRepository extends JpaRepository<Follow, FollowId>,
        QuerydslPredicateExecutor<Follow>,
        FollowRepositoryCustom {

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

    long countByFollowerId(Long followerId);

    long countByFollowingId(Long followingId);
}