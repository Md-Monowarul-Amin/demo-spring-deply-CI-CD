package com.example.demo.service.impl;

import com.example.demo.dto.request.CreateCommentRequest;
import com.example.demo.dto.response.CommentResponse;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.enam.LikeTargetType;
import com.example.demo.enam.Visibility;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.mapper.CommentMapper;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository    postRepository;
    private final UserRepository    userRepository;
    private final LikeRepository    likeRepository;
    private final CommentMapper     commentMapper;

    @Override
    @Transactional
    public CommentResponse create(Long postId, CreateCommentRequest request, Long authorId) {
        Post post     = findVisiblePost(postId, authorId);
        User author   = findUserById(authorId);

        Comment parent = null;
        int depth      = 0;

        if (request.getParentId() != null) {
            parent = findCommentById(request.getParentId());

            // Ensure parent belongs to the same post
            if (!parent.getPost().getId().equals(postId)) {
                throw new BadRequestException("Parent comment does not belong to this post");
            }

            // Cap depth at 1 — no replies to replies
            if (parent.getDepth() >= 1) {
                throw new BadRequestException("Replies to replies are not supported");
            }

            depth = 1;
        }

        Comment comment = commentMapper.toEntity(request);
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setParent(parent);
        comment.setDepth(depth);

        Comment saved = commentRepository.save(comment);
        return enrichWithViewerContext(saved, authorId);
    }

    @Override
    @Transactional
    public void delete(Long commentId, Long currentUserId) {
        Comment comment = findCommentById(commentId);

        if (!comment.getAuthor().getId().equals(currentUserId)) {
            throw new UnauthorizedException("You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CommentResponse> getCommentsByPost(
            Long postId, Long currentUserId, int page, int size) {

        findVisiblePost(postId, currentUserId);

        Page<CommentResponse> responsePage = commentRepository
                .findTopLevelCommentsByPost(postId, PageRequest.of(page, size))
                .map(comment -> enrichWithViewerContext(comment, currentUserId));

        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CommentResponse> getRepliesByComment(
            Long commentId, Long currentUserId, int page, int size) {

        Comment parent = findCommentById(commentId);

        if (parent.getDepth() != 0) {
            throw new BadRequestException("Replies can only be fetched for top-level comments");
        }

        Page<CommentResponse> responsePage = commentRepository
                .findRepliesByParent(commentId, PageRequest.of(page, size))
                .map(comment -> enrichWithViewerContext(comment, currentUserId));

        return PageResponse.from(responsePage);
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    private CommentResponse enrichWithViewerContext(Comment comment, Long viewerId) {
        CommentResponse response = commentMapper.toResponse(comment);

        boolean liked = likeRepository.existsByUserIdAndTargetIdAndTargetType(
                viewerId, comment.getId(), LikeTargetType.COMMENT);

        long replyCount = comment.getDepth() == 0
                ? commentRepository.findRepliesByParent(
                        comment.getId(), PageRequest.of(0, 1)).getTotalElements()
                : 0;

        return CommentResponse.builder()
                .id(response.getId())
                .author(response.getAuthor())
                .content(response.getContent())
                .depth(response.getDepth())
                .likeCount(response.getLikeCount())
                .parentId(response.getParentId())
                .createdAt(response.getCreatedAt())
                .updatedAt(response.getUpdatedAt())
                .likedByCurrentUser(liked)
                .replyCount((int) replyCount)
                .build();
    }

    private Post findVisiblePost(Long postId, Long viewerId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", postId));

        if (post.getVisibility() == Visibility.PRIVATE
                && !post.getAuthor().getId().equals(viewerId)) {
            throw new ResourceNotFoundException("Post", postId);
        }

        return post;
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", commentId));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }
}
