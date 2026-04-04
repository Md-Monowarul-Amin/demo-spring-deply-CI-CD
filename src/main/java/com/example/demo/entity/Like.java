package com.example.demo.entity;

import com.example.demo.enam.LikeTargetType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(
        name = "likes",
        indexes = {
                @Index(name = "idx_likes_target", columnList = "target_id, target_type"),
                @Index(name = "idx_likes_user_target", columnList = "user_id, target_id, target_type")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_likes_user_target",
                        columnNames = {"user_id", "target_id", "target_type"}
                )
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "like_seq")
    @SequenceGenerator(name = "like_seq", sequenceName = "like_seq", allocationSize = 50)
    @Column(updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Polymorphic FK — points to either a Post or Comment ID.
     * Use target_type to resolve which table to join.
     */
    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 10)
    private LikeTargetType targetType;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}