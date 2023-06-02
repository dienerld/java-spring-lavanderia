package com.example.apilavanderia.dtos;

import com.example.apilavanderia.models.Apartment;

import java.util.List;

public record OutputApartmentWithHistory(String number, String name, String phone, List<OutputBookingWithoutApartment> bookings ) {
    public OutputApartmentWithHistory(Apartment apt) {
        this(apt.getNumber(),
                apt.getNameResident(),
                apt.getPhone(),
                apt.getBookings().stream().map(OutputBookingWithoutApartment::new).toList()
        );
    }
}
