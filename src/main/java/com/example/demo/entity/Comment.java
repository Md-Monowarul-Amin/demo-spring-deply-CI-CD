package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "comments",
        indexes = {
                @Index(name = "idx_comments_post_created", columnList = "post_id, created_at DESC"),
                @Index(name = "idx_comments_parent_id", columnList = "parent_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    /**
     * Null for top-level comments; points to a top-level comment for replies.
     * Depth is capped at 1 (comment → reply) enforced at the service layer.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Comment> replies = new HashSet<>();

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /**
     * 0 = top-level comment, 1 = reply.
     * Stored to avoid recursive parent traversal queries.
     */
    @Column(nullable = false)
    @Builder.Default
    private int depth = 0;

    /**
     * Denormalized like counter — updated by application events.
     */
    @Column(name = "like_count", nullable = false)
    @Builder.Default
    private int likeCount = 0;
}