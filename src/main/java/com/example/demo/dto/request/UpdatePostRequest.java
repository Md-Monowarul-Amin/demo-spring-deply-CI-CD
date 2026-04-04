package com.example.demo.dto.request;

import com.example.demo.enam.Visibility;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePostRequest {

    @Size(max = 5000, message = "Post content must not exceed 5000 characters")
    private String content;

    private Visibility visibility;
}
