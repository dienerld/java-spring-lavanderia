package com.example.apilavanderia.Dtos;

import com.example.apilavanderia.models.Apartment;

public record OutputApartment(String number, String name, String phone) {

    public OutputApartment(Apartment apt) {
        this(apt.getNumber(), apt.getNameResident(), apt.getPhone());
    }
}
