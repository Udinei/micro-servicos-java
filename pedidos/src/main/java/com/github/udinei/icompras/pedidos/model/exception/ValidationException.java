package com.github.udinei.icompras.pedidos.model.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException{
    String field;
    String message;

    public ValidationException(String field, String message) {
        super(message);
        this.field = field;
        this.message = message;
    }
}
