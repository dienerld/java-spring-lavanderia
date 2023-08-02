package com.example.apilavanderia.controllers;

import com.example.apilavanderia.customExceptions.AlreadyExistsException;
import com.example.apilavanderia.customExceptions.UnauthorizedException;
import com.example.apilavanderia.dtos.*;
import com.example.apilavanderia.models.Apartment;
import com.example.apilavanderia.repositories.specifications.ApartmentsSpecifications;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.apilavanderia.repositories.ApartmentRepository;

import java.util.List;

@RestController
@RequestMapping("/apartments")

public class ApartmentController {
    @Autowired
    private ApartmentRepository repository;


    @GetMapping
    public ResponseEntity<List<OutputApartment>> getAll(
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String hasLogged
    ) {
        var apartments = repository.findAll(ApartmentsSpecifications.filters(phone, hasLogged));

        return ResponseEntity.ok().body(apartments.stream().map(OutputApartment::new).toList());
    }

    @ApiResponse(useReturnTypeSchema = true, responseCode = "200", description = "Retorna o apartamento com aquele numero.")
    @GetMapping("/{number}")
    public ResponseEntity<OutputApartmentWithHistory> getOne(@PathVariable String number, @RequestHeader("AuthToken") String token) {
        var apt = repository.getReferenceById(number);

        if (!apt.isAuthenticated(token)) {
            throw new UnauthorizedException("Token Inválido");
        }

        return ResponseEntity.ok().body(new OutputApartmentWithHistory(apt));

    }

    @PostMapping
    public ResponseEntity<OutputApartment> create(@RequestBody @Valid CreateApartment newApt) {

        var aptExists = repository.existsById(newApt.number());
        if (aptExists) {
            throw new AlreadyExistsException("Apartamento ja existe");
        }
        var apt = new Apartment(newApt);
        repository.save(apt);
        return ResponseEntity.ok().body(new OutputApartment(apt));
    }

    @PutMapping("/{number}")
    public ResponseEntity<OutputApartment> update(@RequestBody UpdateApartment dto,
                                                  @PathVariable String number,
                                                  @RequestHeader("AuthToken") String token
    ) {
        var apt = repository.getReferenceById(number);

        if (apt.isAuthenticated(token)) {
            throw new UnauthorizedException("Token Inválido");
        }

        if (dto.phone() != null) apt.setPhone(dto.phone());
        if (dto.nameResident() != null) apt.setNameResident(dto.nameResident());
        if (dto.password() != null) {
            if (dto.password().length() < 6) {
                throw new RuntimeException("Senha muito curta");
            }
            apt.setPassword(dto.password());
        }
        repository.save(apt);

        return ResponseEntity.ok().body(new OutputApartment(apt));

    }

}
