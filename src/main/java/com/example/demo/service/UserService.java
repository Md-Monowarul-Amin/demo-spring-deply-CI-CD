package com.example.demo.service;


import com.example.demo.dto.UserCreateRequestDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserCreateRequestDTO requestDTO);

    UserDTO getUserById(Long id);

    // UserDTO getUserByUsername(String username);

    UserDTO updateUser(Long id, UserUpdateRequestDTO requestDTO);

    void deleteUser(Long id);

    Page<UserDTO> getAllUsers(Pageable pageable);

    // Optional: search
    // List<UserDTO> searchUsers(String keyword);
}
