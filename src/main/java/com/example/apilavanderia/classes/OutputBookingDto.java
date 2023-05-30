package com.example.apilavanderia.classes;

import com.example.apilavanderia.enums.Machine;

import java.time.LocalDate;

public record OutputBookingDto(Apartment apartment, LocalDate date, String hour, Machine machine, int id) {
    public OutputBookingDto(Booking booking) {
        this(booking.getApartment(), booking.getDate(), booking.getHour().hourToString(), booking.getMachine(), booking.getId());
    }
}
