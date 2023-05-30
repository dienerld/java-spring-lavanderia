package com.example.apilavanderia.controllers;

import com.example.apilavanderia.classes.Apartment;
import com.example.apilavanderia.database.Database;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apartments")
public class ApartmentController {
    Database database;

    public ApartmentController() {
        database = new Database();
    }

    @GetMapping
    public List<Apartment> getAll() {
        return database.getApartments();
    }

    @PostMapping
    public Apartment create(@RequestBody Apartment newApt) {
        var aptExists = database.getApartments().stream().filter(apt -> apt.getNumber().equalsIgnoreCase(newApt.getNumber())).findFirst();
        if(aptExists.isPresent()) {
            return null;
        }
        database.addApartment(newApt);
        return newApt;
    }


}
