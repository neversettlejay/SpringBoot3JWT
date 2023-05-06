package com.jaytech.security.service.definition;

public interface JwtService {
    String extractUsernameFromJwtToken(String jwtToken);
}
