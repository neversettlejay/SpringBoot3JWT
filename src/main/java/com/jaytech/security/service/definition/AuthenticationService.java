package com.jaytech.security.service.definition;

import com.jaytech.security.authentication.AuthenticationRequest;
import com.jaytech.security.authentication.AuthenticationResponse;
import com.jaytech.security.authentication.RegisterRequest;
import com.jaytech.security.roles.dto.CustomHttpResponse;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    CustomHttpResponse registerUser(RegisterRequest registerRequest);

    CustomHttpResponse authenticateUser(AuthenticationRequest authenticationRequest);
}
