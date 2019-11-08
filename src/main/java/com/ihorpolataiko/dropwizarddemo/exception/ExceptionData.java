package com.ihorpolataiko.dropwizarddemo.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ExceptionData {
    private int code;
    private String message;
}
