package com.example.demo.mapper;

import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AuthMapper {

    // passwordHash is set at service layer after encoding — never map raw password
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "avatarUrl", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "followers", ignore = true)
    @Mapping(target = "following", ignore = true)
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    User toEntity(RegisterRequest request);
}
