package com.example.UserService.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {
    String email;
    String password;
}
