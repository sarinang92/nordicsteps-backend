package com.myproject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password; // plain text input, will be hashed
    private String phoneNumber;
    private String address;
    private String city;
    private String postalCode;
    private String country;
}
