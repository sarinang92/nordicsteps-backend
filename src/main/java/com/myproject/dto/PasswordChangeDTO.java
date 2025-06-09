package com.myproject.dto;

import lombok.Data;
import lombok.ToString;

@Data
public class PasswordChangeDTO {
    private Long userId;

    @ToString.Exclude
    private String oldPassword;

    @ToString.Exclude
    private String newPassword;
}
