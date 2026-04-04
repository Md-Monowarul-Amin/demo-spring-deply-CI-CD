package com.example.demo.dto.response;

import com.example.demo.enam.Visibility;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
public class PostResponse {
    private Long id;
    private UserSummaryResponse author;
    private String content;
    private Visibility visibility;
    private int likeCount;
    private int commentCount;
    private boolean likedByCurrentUser;
    private List<PostMediaResponse> media;
    private Instant createdAt;
    private Instant updatedAt;
}
