package com.jaytech.security.controller.authentication;

import com.jaytech.security.configurations.PropertyConfigurations;
import com.jaytech.security.models.payload.users.AuthenticationRequest;
import com.jaytech.security.models.payload.users.RegisterRequest;
import com.jaytech.security.models.payload.transfer.CustomHttpResponse;
import com.jaytech.security.service.implementation.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/back-end/user")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final PropertyConfigurations propertyConfigurations;

    //Register user
    @PostMapping("/sign-up")
    public ResponseEntity<CustomHttpResponse> registerUser(
            @RequestBody RegisterRequest registerRequest
    ) {
        CustomHttpResponse response = authenticationService.registerUser(registerRequest);

        return response.getHttpStatus().is4xxClientError() ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response) :
                ResponseEntity.ok(response);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<CustomHttpResponse> authenticateUser(
            @RequestBody AuthenticationRequest authenticationRequest
    ) {
        return ResponseEntity.ok(authenticationService.authenticateUser(authenticationRequest));
    }

}
