package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FollowRepositoryCustom {

    // Paginated list of users that userId is following
    Page<User> findFollowing(Long userId, Pageable pageable);

    // Paginated list of users that follow userId
    Page<User> findFollowers(Long userId, Pageable pageable);
}