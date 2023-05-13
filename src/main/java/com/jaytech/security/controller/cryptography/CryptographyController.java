package com.jaytech.security.controller.cryptography;

import com.jaytech.security.configurations.PropertyConfigurations;
import com.jaytech.security.cryptograpy.decryption.DecryptionService;
import com.jaytech.security.cryptograpy.utility.GenerateKeys;
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
    private final GenerateKeys generateKeys;
    private final DecryptionService decryptionService;


    @PostMapping("/generate/keys")
    public ResponseEntity<CustomHttpResponse> createKeys(@RequestParam int keylength, HttpServletRequest request, HttpServletResponse response) {
        String adminDataAccessKey = request.getHeader("admin-data-access-key");
        if (propertyConfigurations.getAdminAccessKey().equals(adminDataAccessKey))
            return ResponseEntity.ok(CustomHttpResponse.builder().httpStatus(HttpStatus.OK).httpStatusCode(HttpStatus.OK.value()).message("Success").data(generateKeys.initializeKeys(keylength)).build());
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(CustomHttpResponse.builder().httpStatus(HttpStatus.FORBIDDEN).httpStatusCode(HttpStatus.FORBIDDEN.value()).message("Forbidden").data(null).build());
    }

    @PostMapping("/generate/encrypt")
    public ResponseEntity<CustomHttpResponse> encrypt(@RequestBody String plaintext, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String adminDataAccessKey = request.getHeader("admin-data-access-key");
        if (!propertyConfigurations.getAdminAccessKey().equals(adminDataAccessKey))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(CustomHttpResponse.builder().httpStatus(HttpStatus.FORBIDDEN).httpStatusCode(HttpStatus.FORBIDDEN.value()).message("Forbidden").build());

        PrivateKey privateKey = decryptionService.getPrivate("KeyPair/privateKey");

        String encryptedMessage = decryptionService.encrypt(plaintext, privateKey);

        return ResponseEntity.ok(CustomHttpResponse.builder().httpStatus(HttpStatus.OK).httpStatusCode(HttpStatus.OK.value()).message("Success").data("Encrypted data: '"+encryptedMessage+"'").build());
    }


    @PostMapping("/generate/decrypt")
    public ResponseEntity<CustomHttpResponse> decrypt(@RequestBody String ciphertext, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String adminDataAccessKey = request.getHeader("admin-data-access-key");
        if (!propertyConfigurations.getAdminAccessKey().equals(adminDataAccessKey))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(CustomHttpResponse.builder().httpStatus(HttpStatus.FORBIDDEN).httpStatusCode(HttpStatus.FORBIDDEN.value()).message("Forbidden").build());

        PublicKey publicKey = decryptionService.getPublic("KeyPair/publicKey");

        String decryptedMessage = decryptionService.decrypt(ciphertext, publicKey);

        return ResponseEntity.ok(CustomHttpResponse.builder().httpStatus(HttpStatus.OK).httpStatusCode(HttpStatus.OK.value()).message("Success").data("Decrypted message :'"+ decryptedMessage+"'").build());
    }


}
