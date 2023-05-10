package com.jaytech.security.test;

import com.jaytech.security.models.payload.transfer.CustomHttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/back-end")
public class TestController {

    @GetMapping
    public ResponseEntity<CustomHttpResponse> test() {
        return ResponseEntity.ok(CustomHttpResponse.builder().httpStatus(HttpStatus.OK).httpStatusCode(HttpStatus.OK.value()).message("Success").build());
    }

}
