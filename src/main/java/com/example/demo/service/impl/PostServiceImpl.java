package com.example.demo.service.impl;

import com.example.demo.dto.request.CreatePostRequest;
import com.example.demo.dto.request.UpdatePostRequest;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.PostResponse;
import com.example.demo.entity.Post;
import com.example.demo.entity.PostMedia;
import com.example.demo.entity.User;
import com.example.demo.enam.LikeTargetType;
import com.example.demo.enam.Visibility;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.mapper.PostMapper;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.PostMediaRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository      postRepository;
    private final UserRepository      userRepository;
    private final LikeRepository      likeRepository;
    private final PostMediaRepository postMediaRepository;
    private final PostMapper          postMapper;

    @Override
    @Transactional
    public PostResponse create(CreatePostRequest request, Long authorId) {
        if ((request.getContent() == null || request.getContent().isBlank())
                && request.getMedia().isEmpty()) {
            throw new BadRequestException("Post must have content or at least one media item");
        }

        User author = findUserById(authorId);

        Post post = postMapper.toEntity(request);
        post.setAuthor(author);
        Post saved = postRepository.save(post);

        // Persist media items linked to the saved post
        if (!request.getMedia().isEmpty()) {
            List<PostMedia> mediaEntities = IntStream.range(0, request.getMedia().size())
                    .mapToObj(i -> {
                        CreatePostRequest.MediaItem item = request.getMedia().get(i);
                        return PostMedia.builder()
                                .post(saved)
                                .s3Key(item.getS3Key())
                                .mediaType(item.getMediaType())
                                .sortOrder(i)
                                .build();
                    })
                    .toList();
            postMediaRepository.saveAll(mediaEntities);
            saved.getMedia().addAll(mediaEntities);
        }

        return enrichWithViewerContext(saved, authorId);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getById(Long postId, Long currentUserId) {
        Post post = findPostById(postId);
        assertVisible(post, currentUserId);
        return enrichWithViewerContext(post, currentUserId);
    }

    @Override
    @Transactional
    public PostResponse update(Long postId, UpdatePostRequest request, Long currentUserId) {
        Post post = findPostById(postId);
        assertOwner(post, currentUserId);

        postMapper.updatePostFromRequest(request, post);
        postRepository.save(post);

        return enrichWithViewerContext(post, currentUserId);
    }

    @Override
    @Transactional
    public void delete(Long postId, Long currentUserId) {
        Post post = findPostById(postId);
        assertOwner(post, currentUserId);
        postRepository.delete(post);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PostResponse> getPostsByAuthor(
            Long authorId, Long currentUserId, int page, int size) {

        findUserById(authorId);

        Page<PostResponse> responsePage = postRepository
                .findPostsByAuthor(authorId, currentUserId, PageRequest.of(page, size))
                .map(post -> enrichWithViewerContext(post, currentUserId));

        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PostResponse> getFeedForUser(Long currentUserId, int page, int size) {
        Page<PostResponse> responsePage = postRepository
                .findFeedForUser(currentUserId, PageRequest.of(page, size))
                .map(post -> enrichWithViewerContext(post, currentUserId));

        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PostResponse> getPublicFeed(Long currentUserId, int page, int size) {
        Page<PostResponse> responsePage = postRepository
                .findPublicPosts(PageRequest.of(page, size))
                .map(post -> enrichWithViewerContext(post, currentUserId));

        return PageResponse.from(responsePage);
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    private PostResponse enrichWithViewerContext(Post post, Long viewerId) {
        PostResponse response = postMapper.toResponse(post);

        boolean liked = likeRepository.existsByUserIdAndTargetIdAndTargetType(
                viewerId, post.getId(), LikeTargetType.POST);

        return PostResponse.builder()
                .id(response.getId())
                .author(response.getAuthor())
                .content(response.getContent())
                .visibility(response.getVisibility())
                .likeCount(response.getLikeCount())
                .commentCount(response.getCommentCount())
                .media(response.getMedia())
                .createdAt(response.getCreatedAt())
                .updatedAt(response.getUpdatedAt())
                .likedByCurrentUser(liked)
                .build();
    }

    private void assertVisible(Post post, Long viewerId) {
        boolean isPrivate = post.getVisibility() == Visibility.PRIVATE;
        boolean isOwner   = post.getAuthor().getId().equals(viewerId);

        if (isPrivate && !isOwner) {
            // Return 404 instead of 403 — do not leak existence of private posts
            throw new ResourceNotFoundException("Post", post.getId());
        }
    }

    private void assertOwner(Post post, Long currentUserId) {
        if (!post.getAuthor().getId().equals(currentUserId)) {
            throw new UnauthorizedException("You do not have permission to modify this post");
        }
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", postId));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }
}
