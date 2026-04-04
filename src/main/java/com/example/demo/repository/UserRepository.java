package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>,
        QuerydslPredicateExecutor<User>,
        UserRepositoryCustom {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}