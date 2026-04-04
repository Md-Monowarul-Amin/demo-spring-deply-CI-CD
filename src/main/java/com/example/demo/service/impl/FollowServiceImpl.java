package com.example.demo.service.impl;

import com.example.demo.dto.response.FollowResponse;
import com.example.demo.entity.Follow;
import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.FollowRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository   userRepository;

    @Override
    @Transactional
    public FollowResponse follow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new BadRequestException("You cannot follow yourself");
        }

        User follower  = findActiveUserById(followerId);
        User following = findActiveUserById(followingId);

        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new DuplicateResourceException("Follow", "followingId", followingId);
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();

        followRepository.save(follow);

        return FollowResponse.builder()
                .userId(followingId)
                .following(true)
                .followerCount(followRepository.countByFollowingId(followingId))
                .build();
    }

    @Override
    @Transactional
    public FollowResponse unfollow(Long followerId, Long followingId) {
        if (!followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new BadRequestException("You are not following this user");
        }

        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);

        return FollowResponse.builder()
                .userId(followingId)
                .following(false)
                .followerCount(followRepository.countByFollowingId(followingId))
                .build();
    }

    // ── Shared helper ────────────────────────────────────────────────────────

    private User findActiveUserById(Long userId) {
        return userRepository.findByIdWithStats(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }
}
