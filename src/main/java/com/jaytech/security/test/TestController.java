package com.jaytech.security.test;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

@RestController
@RequestMapping("/test/back-end")
@Slf4j
@RequiredArgsConstructor
public class TestController {
    @GetMapping
    public ResponseEntity<CustomHttpResponse> test() {
        return ResponseEntity.ok(CustomHttpResponse.builder().httpStatus(HttpStatus.OK).httpStatusCode(HttpStatus.OK.value()).message("Success").build());
    }

}
