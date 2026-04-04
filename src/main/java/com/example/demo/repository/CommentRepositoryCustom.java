package com.example.demo.repository;

import com.example.demo.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {

    // Top-level comments (depth=0) for a post, newest first
    Page<Comment> findTopLevelCommentsByPost(Long postId, Pageable pageable);

    // Replies (depth=1) for a specific parent comment
    Page<Comment> findRepliesByParent(Long parentCommentId, Pageable pageable);
}