package com.example.demo.dto.response;

import com.example.demo.enam.LikeTargetType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeResponse {
    private Long targetId;
    private LikeTargetType targetType;
    private int likeCount;
    private boolean liked;           // current state after toggle
}
