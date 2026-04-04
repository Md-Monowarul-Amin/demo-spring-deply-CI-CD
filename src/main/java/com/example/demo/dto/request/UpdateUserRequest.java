package com.example.demo.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    private String avatarUrl;
}
