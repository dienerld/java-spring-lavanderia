package com.example.apilavanderia.repositories;

import com.example.apilavanderia.enums.Machine;
import com.example.apilavanderia.enums.Shift;
import com.example.apilavanderia.models.Apartment;
import com.example.apilavanderia.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID>, JpaSpecificationExecutor<Booking> {

    public List<Booking> getBookingsByApartment(Apartment apartment);

    public List<Booking> findByApartmentAndDateBetween(Apartment apt, LocalDate initial, LocalDate end);

    public List<Booking> findByDateAndMachineAndHour(LocalDate date, Machine machine, Shift hour);

}
