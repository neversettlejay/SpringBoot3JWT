package com.jaytech.security.service.definition;

import com.jaytech.security.models.payload.users.AuthenticationRequest;
import com.jaytech.security.models.payload.users.RegisterRequest;
import com.jaytech.security.models.payload.transfer.CustomHttpResponse;

public interface AuthenticationService {
    CustomHttpResponse registerUser(RegisterRequest registerRequest);

    CustomHttpResponse authenticateUser(AuthenticationRequest authenticationRequest);
}
