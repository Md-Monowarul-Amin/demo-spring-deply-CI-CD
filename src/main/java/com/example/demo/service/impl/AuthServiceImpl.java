package com.example.demo.service.impl;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.response.AuthResponse;
import com.example.demo.entity.User;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.TokenException;
import com.example.demo.mapper.AuthMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtils;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository        userRepository;
    private final PasswordEncoder       passwordEncoder;
    private final JwtUtils              jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final AuthMapper            authMapper;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }

        User user = authMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        UserPrincipal principal    = UserPrincipal.from(user);
        String        accessToken  = jwtUtils.generateAccessToken(principal);
        String        refreshToken = jwtUtils.generateRefreshToken(principal);

        return AuthResponse.of(accessToken, refreshToken, user.getId(), user.getEmail());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserPrincipal principal    = (UserPrincipal) authentication.getPrincipal();
        String        accessToken  = jwtUtils.generateAccessToken(principal);
        String        refreshToken = jwtUtils.generateRefreshToken(principal);

        return AuthResponse.of(accessToken, refreshToken, principal.getId(), principal.getEmail());
    }
}
