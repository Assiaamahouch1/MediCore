package com.example.cabinetservice.controller;

import com.example.cabinetservice.dto.CabinetResponse;
import com.example.cabinetservice.dto.SubscriptionStatusResponse;
import com.example.cabinetservice.service.CabinetService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CabinetController.class)
class CabinetControllerTest {

    @Autowired MockMvc mvc;
    @MockBean CabinetService service;

    @Test
    void create_returns201() throws Exception {
        CabinetResponse resp = new CabinetResponse(1L, null, "Cab A", null, null, null, false, null);
        Mockito.when(service.create(Mockito.any())).thenReturn(resp);

        mvc.perform(post("/cabinets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
          {"nom":"Cab A","specialite":"Gen","adresse":"Rue 1","tel":"0612345678"}
        """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/cabinets/1"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void status_returnsFlags() throws Exception {
        Mockito.when(service.status(1L)).thenReturn(new SubscriptionStatusResponse(true, false, 10));

        mvc.perform(get("/cabinets/1/subscription/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.expired").value(false))
                .andExpect(jsonPath("$.daysRemaining").value(10));
    }

    @Test
    void setExpiration_updatesAndReturnsResponse() throws Exception {
        var exp = LocalDate.now().plusMonths(2);
        Mockito.when(service.setExpiration(Mockito.eq(1L), Mockito.any()))
                .thenReturn(new CabinetResponse(1L, null, "Cab A", null, null, null, true, exp));

        mvc.perform(post("/cabinets/1/subscription/set-expiration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"expirationDate\":\"" + exp + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.service_actif").value(true))
                .andExpect(jsonPath("$.date_expiration_service").value(exp.toString()));
    }
}