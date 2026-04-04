package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Getter;

// Lightweight user info embedded inside posts, comments, likes lists
@Getter
@Builder
public class UserSummaryResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String avatarUrl;
}
