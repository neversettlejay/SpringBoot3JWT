package com.jaytech.security.models.payload.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String country;
    private String state;
    private LocalDate birthDate;
    private Long phoneNumber;
    private String password;
    private List<String> roles;
}
