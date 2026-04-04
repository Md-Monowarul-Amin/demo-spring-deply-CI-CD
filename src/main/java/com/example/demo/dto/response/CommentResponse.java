package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class CommentResponse {
    private Long id;
    private UserSummaryResponse author;
    private String content;
    private int depth;
    private int likeCount;
    private boolean likedByCurrentUser;
    private Long parentId;           // null for top-level comments
    private int replyCount;          // number of replies to this comment
    private Instant createdAt;
    private Instant updatedAt;
}
