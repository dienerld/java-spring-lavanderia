package com.example.apilavanderia.CustomExceptions;

public class BookingException extends RuntimeException {

    public BookingException(String message){

        super(message);
    }
}
