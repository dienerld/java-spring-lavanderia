package com.example.apilavanderia.dtos;

import jakarta.validation.constraints.NotBlank;


public record UpdateApartment(

        String nameResident,

        String phone,

        String password) {

}
