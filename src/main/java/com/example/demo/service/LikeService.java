package com.example.demo.service;

import com.example.demo.dto.response.LikeResponse;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.UserSummaryResponse;
import com.example.demo.enam.LikeTargetType;

public interface LikeService {

    // Toggles like — likes if not liked, unlikes if already liked
    LikeResponse toggle(Long userId, Long targetId, LikeTargetType targetType);

    // Who liked a post or comment
    PageResponse<UserSummaryResponse> getLikedBy(Long targetId, LikeTargetType targetType, int page, int size);
}
