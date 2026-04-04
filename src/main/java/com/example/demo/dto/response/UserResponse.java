package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

// Full user profile response
@Getter
@Builder
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String avatarUrl;
    private long followerCount;
    private long followingCount;
    private boolean isFollowing;      // is the current viewer following this user?
    private Instant createdAt;
}
