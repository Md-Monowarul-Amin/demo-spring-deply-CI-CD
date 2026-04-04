package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.enam.LikeTargetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LikeRepositoryCustom {

    // Fetch users who liked a specific post or comment — for "X and Y liked this"
    Page<User> findUserWhoLiked(Long targetId, LikeTargetType targetType, Pageable pageable);

    // Count likes for a target — used when Redis counter is unavailable
    long countByTarget(Long targetId, LikeTargetType targetType);
}