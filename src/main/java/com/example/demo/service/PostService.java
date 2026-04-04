package com.example.demo.service;

import com.example.demo.dto.request.CreatePostRequest;
import com.example.demo.dto.request.UpdatePostRequest;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.PostResponse;

public interface PostService {

    PostResponse create(CreatePostRequest request, Long authorId);

    PostResponse getById(Long postId, Long currentUserId);

    PostResponse update(Long postId, UpdatePostRequest request, Long currentUserId);

    void delete(Long postId, Long currentUserId);

    // Author's own posts — respects visibility based on viewer
    PageResponse<PostResponse> getPostsByAuthor(Long authorId, Long currentUserId, int page, int size);

    // Home feed — posts from followed users
    PageResponse<PostResponse> getFeedForUser(Long currentUserId, int page, int size);

    // Public feed — all public posts, for explore/discover
    PageResponse<PostResponse> getPublicFeed(Long currentUserId, int page, int size);
}
