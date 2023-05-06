package com.jaytech.security.roles.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomHttpResponse<T>  {
    private HttpStatus httpStatus;
    private String message;
    private T data;
}
