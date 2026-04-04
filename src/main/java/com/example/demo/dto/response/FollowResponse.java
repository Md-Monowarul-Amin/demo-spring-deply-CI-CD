package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowResponse {
    private Long userId;
    private boolean following;       // current state after toggle
    private long followerCount;
}
