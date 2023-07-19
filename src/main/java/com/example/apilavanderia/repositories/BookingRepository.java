package com.example.apilavanderia.repositories;

import com.example.apilavanderia.models.Apartment;
import com.example.apilavanderia.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    public List<Booking> getBookingsByApartment(Apartment apartment);

}
