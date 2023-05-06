package com.jaytech.security.authentication;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationRequest {
private String username;
private String password;
}
