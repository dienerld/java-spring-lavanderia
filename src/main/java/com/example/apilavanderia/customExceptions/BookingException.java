package com.example.apilavanderia.customExceptions;

public class BookingException extends RuntimeException {

    public BookingException(String message) {
        super(message);
    }
}
