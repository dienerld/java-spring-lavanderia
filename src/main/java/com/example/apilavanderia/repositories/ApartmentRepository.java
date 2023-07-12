package com.example.apilavanderia.repositories;

import com.example.apilavanderia.models.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApartmentRepository extends JpaRepository<Apartment, String> {
}
