package com.example.apilavanderia.controllers;

import com.example.apilavanderia.dtos.CreateBooking;
import com.example.apilavanderia.dtos.OutputApartment;
import com.example.apilavanderia.dtos.OutputBooking;
import com.example.apilavanderia.dtos.ResponseError;
import com.example.apilavanderia.enums.Machine;
import com.example.apilavanderia.enums.Shift;
import com.example.apilavanderia.models.Apartment;
import com.example.apilavanderia.models.Booking;
import com.example.apilavanderia.repositories.ApartmentRepository;
import com.example.apilavanderia.repositories.BookingRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test") // seleciona o application-test.properties
@SpringBootTest // indica para o spring iniciar com configuração de teste
@AutoConfigureMockMvc  // configura uma servidor para fazer requisição para os controlers
@AutoConfigureJsonTesters // configura o uso do JacksonTester
class BookingControllerTest {

    @Autowired
    private MockMvc mvc; // responsavel por fazer as requisições

    @Autowired
    private JacksonTester<CreateBooking> jsonCreate;

    @Autowired
    private JacksonTester<OutputBooking> jsonOutput;

    @Autowired
    private JacksonTester<ResponseError> jsonError;

    @Autowired
    private BookingRepository repository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    // ao inicio de cada teste limpa as tabelas do banco de dados
    @BeforeEach
    void clearAllTableEach() {
        repository.deleteAll();
        apartmentRepository.deleteAll();
    }


    @Test // indica que o metodo é de teste (obrigatorio)
    @DisplayName("Cadastrar um novo agendamento")
    // apresenta um nome mais legivel do que faz o teste (opcional)
    @WithMockUser
        // simula um usuario autenticado
    void createNewBooking() throws Exception {
        var ap = createApartment("807");
        var response = mvc.perform(
                MockMvcRequestBuilders.post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                jsonCreate.write(
                                        new CreateBooking(ap.getNumber(), Machine.A, LocalDate.now().plusDays(1), Shift.AFTERNOON)
                                ).getJson()
                        )
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString())
//                .isEqualTo(
//                        jsonOutput.write(
//                                new OutputBooking(
//                                        new OutputApartment(ap),
//                                        LocalDate.now().plusDays(1),
//                                        Shift.AFTERNOON.hourToString(),
//                                        Machine.A,
//                                        UUID.randomUUID()
//                                )
//                        ).getJson()
//                );
    }

    @Test
    @DisplayName("Cadastrar um novo agendamento para mesma maquina mas em outro horario")
    void createNewBooking2() throws Exception {
        var ap = createApartment("202");
        var ap1 = createApartment("102");
        repository.save(new Booking(new CreateBooking(ap.getNumber(), Machine.A, LocalDate.now().plusDays(1), Shift.AFTERNOON), ap));
        var response = mvc.perform(
                MockMvcRequestBuilders.post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                jsonCreate.write(
                                        new CreateBooking(ap1.getNumber(), Machine.A, LocalDate.now().plusDays(1), Shift.MORNING)
                                ).getJson()
                        )
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Deve rejeitar a solicitação de agendamento por causa de periodo minimo")
    void shouldRejectByCooldownNewBooking() throws Exception {
        var ap = createApartment("202");
        repository.save(new Booking(new CreateBooking(ap.getNumber(), Machine.A, LocalDate.now().plusDays(1), Shift.AFTERNOON), ap));

        var response = mvc.perform(
                MockMvcRequestBuilders.post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                jsonCreate.write(
                                        new CreateBooking(ap.getNumber(), Machine.A, LocalDate.now().plusDays(1), Shift.MORNING)
                                ).getJson()
                        )
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonError.write(
                        new ResponseError("User com agendamento no periodo de +-4 dias!", null, "Erro durante acao")
                ).getJson()
        );
    }

    @Test
    @DisplayName("Deve rejeitar a solicitação de agendamento por causa de interferencia de horario")
    void shouldRejectNewBooking() throws Exception {
        var ap = createApartment("202");
        var ap1 = createApartment("201");
        repository.save(new Booking(new CreateBooking(ap.getNumber(), Machine.A, LocalDate.now().plusDays(1), Shift.AFTERNOON), ap));

        var response = mvc.perform(
                MockMvcRequestBuilders.post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                jsonCreate.write(
                                        new CreateBooking(ap1.getNumber(), Machine.A, LocalDate.now().plusDays(1), Shift.AFTERNOON)
                                ).getJson()
                        )
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonError.write(
                        new ResponseError("Maquina ja agendada neste horario.", null, "Erro durante acao")
                ).getJson()
        );
    }

    private Apartment createApartment(String number) {
        return apartmentRepository.save(new Apartment(number));
    }
}