package com.example.demo.mapper;

import com.example.demo.dto.response.PostMediaResponse;
import com.example.demo.entity.PostMedia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PostMediaMapper {

    // url is built from s3Key at service layer before calling mapper
    // so we map the pre-built url string passed separately
    @Mapping(target = "url", source = "s3Key")
    PostMediaResponse toResponse(PostMedia postMedia);

    List<PostMediaResponse> toResponseList(List<PostMedia> mediaList);
}
