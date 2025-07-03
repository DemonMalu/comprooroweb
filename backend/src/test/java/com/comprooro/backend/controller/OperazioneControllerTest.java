package com.comprooro.backend.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.comprooro.backend.dto.OperazioneRequestDTO;
import com.comprooro.backend.dto.OperazioneResponseDTO;
import com.comprooro.backend.service.OperazioneService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class OperazioneControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OperazioneService operazioneService;

    @InjectMocks
    private OperazioneController operazioneController;

    private ObjectMapper objectMapper;
    private OperazioneRequestDTO operazioneRequestDTO;
    private OperazioneResponseDTO operazioneResponseDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(operazioneController).build();
        objectMapper = new ObjectMapper();

        operazioneRequestDTO = new OperazioneRequestDTO();
        operazioneRequestDTO.setDescrizione("Vendita oro");
        operazioneRequestDTO.setTipo(1);
        operazioneRequestDTO.setImporto(BigDecimal.valueOf(1500));
        operazioneRequestDTO.setData(LocalDate.of(2025, 2, 28));
        operazioneRequestDTO.setUsername("testuser");

        operazioneResponseDTO = new OperazioneResponseDTO();
        operazioneResponseDTO.setIdOperazione(1L);
        operazioneResponseDTO.setDescrizione("Vendita oro");
        operazioneResponseDTO.setTipo(1);
        operazioneResponseDTO.setImporto(BigDecimal.valueOf(1500));
        operazioneResponseDTO.setData(LocalDate.of(2025, 2, 28));
        operazioneResponseDTO.setUsername("testuser");
    }

    @Test
    void testRegister() throws Exception {
        when(operazioneService.register(any(OperazioneRequestDTO.class))).thenReturn(operazioneResponseDTO);

        mockMvc.perform(post("/api/home/amministratore/register-operazione")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operazioneRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idOperazione").value(1))
                .andExpect(jsonPath("$.descrizione").value("Vendita oro"))
                .andExpect(jsonPath("$.tipo").value(1))
                .andExpect(jsonPath("$.importo").value(1500))
                .andExpect(jsonPath("$.data").value("2025-02-28"))
                .andExpect(jsonPath("$.username").value("testuser"));

        verify(operazioneService, times(1)).register(any(OperazioneRequestDTO.class));
    }

    @Test
    void testGetAll() throws Exception {
        when(operazioneService.findAll()).thenReturn(Arrays.asList(operazioneResponseDTO));

        mockMvc.perform(get("/api/report/all-operazioni"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].idOperazione").value(1))
                .andExpect(jsonPath("$[0].descrizione").value("Vendita oro"));

        verify(operazioneService, times(1)).findAll();
    }

    @Test
    void testGetOperazioneById_Found() throws Exception {
        when(operazioneService.findById(1L)).thenReturn(Optional.of(operazioneResponseDTO));

        mockMvc.perform(get("/api/report/operazione/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idOperazione").value(1))
                .andExpect(jsonPath("$.descrizione").value("Vendita oro"));

        verify(operazioneService, times(1)).findById(1L);
    }

    @Test
    void testGetOperazioneById_NotFound() throws Exception {
        when(operazioneService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/report/operazione/1"))
                .andExpect(status().isNotFound());

        verify(operazioneService, times(1)).findById(1L);
    }
}
