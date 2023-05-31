package com.example.apilavanderia.database;

import com.example.apilavanderia.models.Apartment;
import com.example.apilavanderia.models.Booking;

import java.util.ArrayList;
import java.util.List;


public class Database {
    public static List<Apartment> apartments = new ArrayList<>();
    public static List<Booking> bookings = new ArrayList<>();


    public List<Apartment> getApartments() {
        return Database.apartments;
    }

    public List<Booking> getBookings() {
        return Database.bookings;
    }

    public void addBookings(Booking booking) {
        bookings.add(booking);
    }

    public void addApartment(Apartment apartment) {
        apartments.add(apartment);
    }

    public Apartment getApartmentByNumber(String number) {
        return apartments.stream()
                .filter(apt -> apt.getNumber().equalsIgnoreCase(number))
                .findFirst()
                .orElseThrow();
    }

}
