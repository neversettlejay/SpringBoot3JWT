package com.jaytech.security.models.payload.users;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationRequest {
private String username;
private String password;
}
