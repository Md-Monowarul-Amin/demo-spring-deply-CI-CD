package com.example.demo.dto.response;

import com.example.demo.enam.MediaType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostMediaResponse {
    private Long id;
    private String url;        // CDN URL built at mapper layer from s3Key
    private MediaType mediaType;
    private int sortOrder;
}
