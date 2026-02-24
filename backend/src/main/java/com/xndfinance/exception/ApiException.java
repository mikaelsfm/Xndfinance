package com.xndfinance.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiException extends RuntimeException {

    private String message;

    private HttpStatus status;

    public ApiException(String message) {
        super(message);

        this.message = message;
    }


    public ApiException(HttpStatus status, String message) {
        this.message = message;
        this.status = status;
    }
}
