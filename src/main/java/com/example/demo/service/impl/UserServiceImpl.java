package com.example.demo.service.impl;

import com.example.demo.dto.request.UpdateUserRequest;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.dto.response.UserSummaryResponse;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.FollowRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository     userRepository;
    private final FollowRepository   followRepository;
    private final UserMapper         userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long userId, Long currentUserId) {
        User user = findActiveUserById(userId);

        UserResponse response = userMapper.toResponse(user);

        return UserResponse.builder()
                .id(response.getId())
                .firstName(response.getFirstName())
                .lastName(response.getLastName())
                .email(response.getEmail())
                .avatarUrl(response.getAvatarUrl())
                .createdAt(response.getCreatedAt())
                .followerCount(followRepository.countByFollowingId(userId))
                .followingCount(followRepository.countByFollowerId(userId))
                .isFollowing(followRepository.existsByFollowerIdAndFollowingId(currentUserId, userId))
                .build();
    }

    @Override
    @Transactional
    public UserResponse updateProfile(Long userId, UpdateUserRequest request, Long currentUserId) {
        if (!userId.equals(currentUserId)) {
            throw new UnauthorizedException("You can only update your own profile");
        }

        User user = findActiveUserById(userId);
        userMapper.updateUserFromRequest(request, user);
        userRepository.save(user);

        return getById(userId, currentUserId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSummaryResponse> searchUsers(String keyword, int limit) {
        return userRepository.searchUsers(keyword, limit)
                .stream()
                .map(userMapper::toSummaryResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserSummaryResponse> getFollowers(Long userId, int page, int size) {
        findActiveUserById(userId);

        Pageable pageable = PageRequest.of(page, size);

        return PageResponse.from(
                followRepository.findFollowers(userId, pageable)
                        .map(userMapper::toSummaryResponse)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserSummaryResponse> getFollowing(Long userId, int page, int size) {
        findActiveUserById(userId);

        Pageable pageable = PageRequest.of(page, size);

        return PageResponse.from(
                followRepository.findFollowing(userId, pageable)
                        .map(userMapper::toSummaryResponse)
        );
    }

    // ── Shared helper ────────────────────────────────────────────────────────

    private User findActiveUserById(Long userId) {
        return userRepository.findByIdWithStats(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }
}
