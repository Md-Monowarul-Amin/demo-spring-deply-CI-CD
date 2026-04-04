package com.example.demo.repository;

import com.example.demo.entity.Post;
import com.example.demo.enam.Visibility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    // Public feed — all PUBLIC posts newest first (for non-followers)
    Page<Post> findPublicPosts(Pageable pageable);

    // Author feed — all posts by a specific author visible to the viewer
    // If viewer == author → PUBLIC + PRIVATE
    // If viewer != author → PUBLIC only
    Page<Post> findPostsByAuthor(Long authorId, Long viewerId, Pageable pageable);

    // Home feed — PUBLIC posts from users that viewerId follows
    Page<Post> findFeedForUser(Long viewerId, Pageable pageable);
}