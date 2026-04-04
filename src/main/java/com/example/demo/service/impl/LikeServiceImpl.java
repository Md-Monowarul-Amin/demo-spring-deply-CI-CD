package com.example.demo.service.impl;

import com.example.demo.dto.response.LikeResponse;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.UserSummaryResponse;
import com.example.demo.entity.Like;
import com.example.demo.entity.User;
import com.example.demo.enam.LikeTargetType;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final UserMapper     userMapper;

    @Override
    @Transactional
    public LikeResponse toggle(Long userId, Long targetId, LikeTargetType targetType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        boolean alreadyLiked = likeRepository
                .existsByUserIdAndTargetIdAndTargetType(userId, targetId, targetType);

        if (alreadyLiked) {
            likeRepository.deleteByUserIdAndTargetIdAndTargetType(userId, targetId, targetType);
        } else {
            Like like = Like.builder()
                    .user(user)
                    .targetId(targetId)
                    .targetType(targetType)
                    .build();
            likeRepository.save(like);
        }

        long updatedCount = likeRepository.countByTarget(targetId, targetType);

        return LikeResponse.builder()
                .targetId(targetId)
                .targetType(targetType)
                .liked(!alreadyLiked)
                .likeCount((int) updatedCount)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserSummaryResponse> getLikedBy(
            Long targetId, LikeTargetType targetType, int page, int size) {

        return PageResponse.from(
                likeRepository.findUserWhoLiked(targetId, targetType, PageRequest.of(page, size))
                        .map(userMapper::toSummaryResponse)
        );
    }
}
