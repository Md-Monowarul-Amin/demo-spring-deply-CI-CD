package com.example.demo.mapper;

import com.example.demo.dto.request.CreateCommentRequest;
import com.example.demo.dto.response.CommentResponse;
import com.example.demo.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {UserMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CommentMapper {

    @Mapping(target = "author", source = "author")
    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "replyCount", ignore = true)       // computed at service layer
    @Mapping(target = "likedByCurrentUser", ignore = true)
    CommentResponse toResponse(Comment comment);

    List<CommentResponse> toResponseList(List<Comment> comments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "depth", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "content", source = "content")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Comment toEntity(CreateCommentRequest request);
}
