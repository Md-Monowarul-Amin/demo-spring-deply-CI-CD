package com.example.demo.mapper;

import com.example.demo.dto.response.UserResponse;
import com.example.demo.dto.response.UserSummaryResponse;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    UserSummaryResponse toSummaryResponse(User user);

    // followerCount, followingCount, isFollowing are computed at the
    // service layer and passed in — MapStruct cannot derive them from entity
    @Mapping(target = "followerCount", ignore = true)
    @Mapping(target = "followingCount", ignore = true)
    @Mapping(target = "isFollowing", ignore = true)
    UserResponse toResponse(User user);

    // Partial update — only non-null fields from request overwrite entity fields
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "followers", ignore = true)
    @Mapping(target = "following", ignore = true)
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateUserFromRequest(
            com.example.demo.dto.request.UpdateUserRequest request,
            @MappingTarget User user
    );
}
