package com.myproject.dto;

import lombok.Data;
import lombok.ToString;

@Data
public class LoginRequestDTO {
    private String email;

    @ToString.Exclude
    private String password;
}
