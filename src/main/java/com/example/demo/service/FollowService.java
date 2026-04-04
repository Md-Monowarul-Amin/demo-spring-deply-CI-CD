package com.example.demo.service;

import com.example.demo.dto.response.FollowResponse;

public interface FollowService {

    FollowResponse follow(Long followerId, Long followingId);

    FollowResponse unfollow(Long followerId, Long followingId);
}
