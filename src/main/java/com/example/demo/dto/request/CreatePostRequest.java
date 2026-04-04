package com.example.demo.dto.request;

import com.example.demo.enam.Visibility;
import com.example.demo.enam.MediaType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostRequest {

    @Size(max = 5000, message = "Post content must not exceed 5000 characters")
    private String content;  // nullable — post can be media-only

    @NotNull
    private Visibility visibility;

    // S3 keys returned after client uploads directly to S3
    // Empty list means text-only post
    private java.util.List<MediaItem> media = new java.util.ArrayList<>();

    @Getter
    @Setter
    public static class MediaItem {
        @NotNull
        private String s3Key;
        @NotNull
        private MediaType mediaType;
        private int sortOrder;
    }
}
