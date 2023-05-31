package com.example.apilavanderia.models;
import com.example.apilavanderia.Dtos.CreateApartment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Apartment {

    private String nameResident;

    private String number;

    private String phone;

    private String password;

    public Apartment(CreateApartment newApt) {
        number = password = nameResident = newApt.number();
    }
}
