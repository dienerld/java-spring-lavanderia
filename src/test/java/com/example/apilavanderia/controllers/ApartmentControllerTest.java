package com.example.apilavanderia.controllers;

import com.example.apilavanderia.dtos.CreateApartment;
import com.example.apilavanderia.dtos.OutputApartment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
class ApartmentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<CreateApartment> jsonCreate;
    @Autowired
    private JacksonTester<OutputApartment> jsonOutput;


    @Test
    @DisplayName("Should return status 500 when not sent body")
    void createApartmentWithNullBody() throws Exception {
        var response = mvc.perform(post("/apartments")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    @Test
    @DisplayName("Should return status 400 when number is null")
    void createApartmentWithNullNumber() throws Exception {
        var response = mvc.perform(
                        post("/apartments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                )
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        ;
    }

    @Test
    @DisplayName("Should return status 400 when number is empty")
    void createApartmentWithEmptyNumber() throws Exception {
        var response = mvc.perform(
                post("/apartments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreate.write(new CreateApartment("")).getJson())
        ).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    @Test
    @DisplayName("Should return status 200 when number is valid")
    void createApartmentWithValidNumber() throws Exception {
        var response = mvc.perform(
                post("/apartments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreate.write(new CreateApartment("123")).getJson())
        ).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}