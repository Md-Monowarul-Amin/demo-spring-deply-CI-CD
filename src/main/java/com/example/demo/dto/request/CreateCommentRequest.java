package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentRequest {

    @NotBlank
    @Size(max = 2000, message = "Comment must not exceed 2000 characters")
    private String content;

    // null = top-level comment, non-null = reply to this comment id
    private Long parentId;
}
