package com.example.demo.repository;

import com.example.demo.entity.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface PostMediaRepository extends JpaRepository<PostMedia, Long>,
        QuerydslPredicateExecutor<PostMedia> {

    // Spring Data handles these — no custom impl needed
    List<PostMedia> findByPostIdOrderBySortOrderAsc(Long postId);

    void deleteByPostId(Long postId);
}