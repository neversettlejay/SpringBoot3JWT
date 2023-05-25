package com.jaytech.security.controller.cryptography;

import com.jaytech.security.configurations.PropertyConfigurations;
import com.jaytech.security.models.payload.transfer.CustomHttpResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

@RestController
@RequestMapping("/back-end/cryptography")
@Slf4j
@RequiredArgsConstructor
public class CryptographyController {
    private final PropertyConfigurations propertyConfigurations;


    @PostMapping("/generate/keys")
    public ResponseEntity<CustomHttpResponse> createKeys(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @PostMapping("/generate/encrypt")
    public ResponseEntity<CustomHttpResponse> encrypt(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return null;
    }


    @PostMapping("/generate/decrypt")
    public ResponseEntity<CustomHttpResponse> decrypt(HttpServletRequest request, HttpServletResponse response) throws Exception {

        return null;
    }


}
