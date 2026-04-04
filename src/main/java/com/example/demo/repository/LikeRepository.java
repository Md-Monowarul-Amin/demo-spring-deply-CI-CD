package com.example.demo.repository;

import com.example.demo.entity.Like;
import com.example.demo.enam.LikeTargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long>,
        QuerydslPredicateExecutor<Like>,
        LikeRepositoryCustom {

    // Spring Data handles these simple lookups
    Optional<Like> findByUserIdAndTargetIdAndTargetType(Long userId, Long targetId, LikeTargetType targetType);

    boolean existsByUserIdAndTargetIdAndTargetType(Long userId, Long targetId, LikeTargetType targetType);

    void deleteByUserIdAndTargetIdAndTargetType(Long userId, Long targetId, LikeTargetType targetType);
}