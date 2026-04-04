package com.example.demo.service;

import com.example.demo.dto.request.CreateCommentRequest;
import com.example.demo.dto.response.CommentResponse;
import com.example.demo.dto.response.PageResponse;

public interface CommentService {

    CommentResponse create(Long postId, CreateCommentRequest request, Long authorId);

    void delete(Long commentId, Long currentUserId);

    PageResponse<CommentResponse> getCommentsByPost(Long postId, Long currentUserId, int page, int size);

    PageResponse<CommentResponse> getRepliesByComment(Long commentId, Long currentUserId, int page, int size);
}
