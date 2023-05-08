package com.jaytech.security.mock;

import com.jaytech.security.roles.dto.CustomHttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/back-end")
public class MockController {

    @GetMapping
    public ResponseEntity<CustomHttpResponse> test() {
        return ResponseEntity.ok(CustomHttpResponse.builder().httpStatus(HttpStatus.ACCEPTED).message("Success").build());
    }

}
