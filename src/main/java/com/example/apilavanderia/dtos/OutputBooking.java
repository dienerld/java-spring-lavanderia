package com.example.apilavanderia.dtos;

import com.example.apilavanderia.models.Apartment;
import com.example.apilavanderia.models.Booking;
import com.example.apilavanderia.enums.Machine;

import java.time.LocalDate;

public record OutputBooking(Apartment apartment, LocalDate date, String hour, Machine machine, int id) {
    public OutputBooking(Booking booking) {
        this(booking.getApartment(), booking.getDate(), booking.getHour().hourToString(), booking.getMachine(), booking.getId());
    }
}
