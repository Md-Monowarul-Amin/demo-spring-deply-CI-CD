package com.example.demo.service;

import com.example.demo.dto.request.UpdateUserRequest;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.dto.response.UserSummaryResponse;

import java.util.List;

public interface UserService {

    UserResponse getById(Long userId, Long currentUserId);

    UserResponse updateProfile(Long userId, UpdateUserRequest request, Long currentUserId);

    List<UserSummaryResponse> searchUsers(String keyword, int limit);

    PageResponse<UserSummaryResponse> getFollowers(Long userId, int page, int size);

    PageResponse<UserSummaryResponse> getFollowing(Long userId, int page, int size);
}
