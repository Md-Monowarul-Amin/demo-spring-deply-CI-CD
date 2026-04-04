package com.example.demo.repository;

import com.example.demo.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryCustom {

    // Search users by name or email — used for @mentions or search
    List<User> searchUsers(String keyword, int limit);

    // Check if follower already follows following
    boolean isFollowing(Long followerId, Long followingId);

    // Fetch user with follower/following counts in one query
    Optional<User> findByIdWithStats(Long userId);
}