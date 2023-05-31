package com.example.apilavanderia.controllers;

import com.example.apilavanderia.dtos.ResponseError;
import com.example.apilavanderia.dtos.UpdateApartment;
import com.example.apilavanderia.models.Apartment;
import com.example.apilavanderia.database.Database;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/apartments")
public class ApartmentController {
    Database database;

    public ApartmentController() {
        database = new Database();
    }

    @GetMapping
    public ResponseEntity getAll() {
        return ResponseEntity.ok().body(database.getApartments());
    }

    @PostMapping
    public ResponseEntity create(@RequestBody Apartment newApt) {
        var aptExists = database.getApartments().stream().filter(apt -> apt.getNumber().equalsIgnoreCase(newApt.getNumber())).findFirst();
        if(aptExists.isPresent()) {
            return ResponseEntity.badRequest().body(new ResponseError("Apartamento já existente!", "."));
        }
        database.addApartment(newApt);
        return ResponseEntity.ok().body(newApt);
    }

    @PutMapping("/{number}")
    public ResponseEntity update(@RequestBody UpdateApartment dto, @PathVariable String number){

        try {


        var apt = database.getApartmentByNumber(number);



        if(dto.phone() != null) apt.setPhone(dto.phone());
        if(dto.nameResident() != null) apt.setNameResident(dto.nameResident());

        return ResponseEntity.ok().body(apt);

        }catch (NoSuchElementException e){
            return ResponseEntity.badRequest().body(new ResponseError(e.getMessage(), e.getClass().getCanonicalName()));
        }

    }

}
