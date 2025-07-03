package com.comprooro.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.comprooro.backend.dto.UtenteRequestDTO;
import com.comprooro.backend.dto.UtenteResponseDTO;
import com.comprooro.backend.service.UtenteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class UtenteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UtenteService utenteService;

    @InjectMocks
    private UtenteController utenteController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(utenteController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testHomeAmministratore() throws Exception {
        mockMvc.perform(get("/api/home/amministratore"))
                .andExpect(status().isOk())
                .andExpect(content().string("Benvenuto nell'area personale dell'amministratore sig. admin"));
    }

    @Test
    @WithMockUser(roles = "OPERATOR")
    void testHomeOperatore() throws Exception {
        mockMvc.perform(get("/api/home/operatore"))
                .andExpect(status().isOk())
                .andExpect(content().string("Benvenuto nell'area personale dell'operatore sig. operatore"));
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void testHomeCliente() throws Exception {
        mockMvc.perform(get("/api/home/cliente"))
                .andExpect(status().isOk())
                .andExpect(content().string("Benvenuto nell'area personale del cliente sig. cliente"));
    }

    @Test
    void testRegister() throws Exception {
        // Simulazione del MultipartFile
        MockMultipartFile documento = new MockMultipartFile("documento", "documento.pdf", "application/pdf", "test content".getBytes());

        UtenteRequestDTO requestDTO = new UtenteRequestDTO("user1", "password", "Nome", "Cognome", 
            LocalDate.of(1990, 1, 1), "Citta", "email@test.com", null, null, "CF123456", "Indirizzo", "CLIENT");

        when(utenteService.register(any(UtenteRequestDTO.class), any(MockMultipartFile.class))).thenReturn("Registrazione completata.");

        mockMvc.perform(multipart("/api/home/operatore/register-cliente")
                .file(documento)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("cliente", objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Registrazione completata."));
    }

    @Test
    void testGetUserByUsername() throws Exception {
        UtenteResponseDTO responseDTO = new UtenteResponseDTO("user1", "Nome", "Cognome", 
            LocalDate.of(1990, 1, 1), "Citta", "email@test.com", null, null, "CF123456", "Indirizzo", "CLIENT");

        when(utenteService.findByUsername("user1")).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(get("/api/report/ricerca-utente/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
    }

    @Test
    @WithMockUser(username = "user1")
    void testVisualizzaProfilo() throws Exception {
        UtenteResponseDTO responseDTO = new UtenteResponseDTO("user1", "Nome", "Cognome", 
            LocalDate.of(1990, 1, 1), "Citta", "email@test.com", null, null, "CF123456", "Indirizzo", "CLIENT");

        when(utenteService.findByUsername("user1")).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(get("/api/home/cliente/profilo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<UtenteResponseDTO> utenti = List.of(
                new UtenteResponseDTO("user1", "Nome", "Cognome", 
                    LocalDate.of(1990, 1, 1), "Citta", "email@test.com", null, null, "CF123456", "Indirizzo", "CLIENT"),
                new UtenteResponseDTO("user2", "Nome2", "Cognome2", 
                    LocalDate.of(1992, 2, 2), "Citta2", "email2@test.com", null, null, "CF654321", "Indirizzo2", "CLIENT")
        );

        when(utenteService.findAllUsers()).thenReturn(utenti);

        mockMvc.perform(get("/api/report/all-utenti"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }
}
