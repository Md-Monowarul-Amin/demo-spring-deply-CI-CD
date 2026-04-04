package com.example.demo.mapper;

import com.example.demo.dto.request.CreatePostRequest;
import com.example.demo.dto.response.PostResponse;
import com.example.demo.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {UserMapper.class, PostMediaMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PostMapper {

    // likedByCurrentUser is resolved at service layer
    @Mapping(target = "author", source = "author")
    @Mapping(target = "media", source = "media")
    @Mapping(target = "likedByCurrentUser", ignore = true)
    PostResponse toResponse(Post post);

    List<PostResponse> toResponseList(List<Post> posts);

    // Map CreatePostRequest → Post entity
    // author, media, and counters are set at service layer
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "media", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "commentCount", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Post toEntity(CreatePostRequest request);

    // Partial update — only content and visibility can be changed
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "media", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "commentCount", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updatePostFromRequest(
            com.example.demo.dto.request.UpdatePostRequest request,
            @MappingTarget Post post
    );
}
