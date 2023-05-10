package com.jaytech.security.models.payload.transfer;

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
    private Integer httpStatusCode;
    private String message;
    private T data;
}
