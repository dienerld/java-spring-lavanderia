package com.example.apilavanderia.dtos;

public record ResponseError(String message, String field, String causa) {
    public ResponseError(String message, String field) {
        this(message, field, null);
    }

    public ResponseError(String s) {
        this(s, null, null);
    }
}
