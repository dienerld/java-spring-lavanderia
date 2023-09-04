package com.example.apilavanderia.dtos;

import com.example.apilavanderia.models.Apartment;

import java.util.UUID;

public record OutputApartment(String number, String name, String phone, String authToken) {

    public OutputApartment(Apartment apt) {
        this(apt.getNumber(), apt.getNameResident(), apt.getPhone(), apt.getTokenLogin());
    }
}
