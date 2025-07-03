package com.comprooro.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
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
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import com.comprooro.backend.dto.MovimentoRequestDTO;
import com.comprooro.backend.dto.MovimentoResponseDTO;
import com.comprooro.backend.service.MovimentoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ExtendWith(MockitoExtension.class)
class MovimentoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MovimentoService movimentoService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private MovimentoController movimentoController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MovimentoRequestDTO movimentoRequestDTO;
    private MovimentoResponseDTO movimentoResponseDTO;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        
        mockMvc = MockMvcBuilders.standaloneSetup(movimentoController).build();

        movimentoRequestDTO = new MovimentoRequestDTO();
        movimentoRequestDTO.setUsername("testuser");
        movimentoRequestDTO.setData(LocalDate.of(2024, 2, 20));
        movimentoRequestDTO.setModalitaPagamento("Bonifico");
        movimentoRequestDTO.setImporto(BigDecimal.valueOf(1000.0));
        movimentoRequestDTO.setAssegnoScannerizzato(null);
        movimentoRequestDTO.setContentType(null);

        movimentoResponseDTO = new MovimentoResponseDTO();
        movimentoResponseDTO.setIdMovimento(1L);
        movimentoResponseDTO.setData(LocalDate.of(2024, 2, 20));
        movimentoResponseDTO.setModalitaPagamento("Bonifico");
        movimentoResponseDTO.setImporto(BigDecimal.valueOf(1000.0));
    }

    @Test
    void testRegisterMovimento() throws Exception {
        // Creiamo una simulazione di un file
        byte[] fileContent = "test file content".getBytes();
        MockMultipartFile assegno = new MockMultipartFile("assegno", "testfile.txt", "text/plain", fileContent);

        when(movimentoService.register(any(MovimentoRequestDTO.class), any(MultipartFile.class))).thenReturn(movimentoResponseDTO);

        mockMvc.perform(multipart("/api/home/operatore/register-movimento")
                    .file(assegno)  // Aggiungiamo il file
                    .param("movimento", objectMapper.writeValueAsString(movimentoRequestDTO))  // Aggiungiamo i parametri
                    .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idMovimento").value(1))
                .andExpect(jsonPath("$.data").value("2024-02-20"))
                .andExpect(jsonPath("$.modalitaPagamento").value("Bonifico"))
                .andExpect(jsonPath("$.importo").value(1000.0));

        verify(movimentoService, times(1)).register(any(MovimentoRequestDTO.class), any(MultipartFile.class));
    }


    @Test
    void testGetStoricoMovimentiCliente() throws Exception {
        List<MovimentoResponseDTO> movimenti = Arrays.asList(movimentoResponseDTO);

        when(authentication.getName()).thenReturn("testuser");
        when(movimentoService.findByUsername("testuser")).thenReturn(movimenti);

        mockMvc.perform(get("/api/home/cliente/storico/{username}", "testuser")
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].data").value("2024-02-20"))
                .andExpect(jsonPath("$[0].importo").value(1000.0));

        verify(movimentoService, times(1)).findByUsername("testuser");
    }

    @Test
    void testGetAllMovimenti() throws Exception {
        List<MovimentoResponseDTO> movimenti = Arrays.asList(movimentoResponseDTO);

        when(movimentoService.findAll()).thenReturn(movimenti);

        mockMvc.perform(get("/api/report/all-movimenti"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].data").value("2024-02-20"))
                .andExpect(jsonPath("$[0].importo").value(1000.0));

        verify(movimentoService, times(1)).findAll();
    }

    @Test
    void testGetMovimentoById() throws Exception {
        when(movimentoService.findById(1L)).thenReturn(Optional.of(movimentoResponseDTO));

        mockMvc.perform(get("/api/report/movimento/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idMovimento").value(1))
                .andExpect(jsonPath("$.data").value("2024-02-20"))
                .andExpect(jsonPath("$.importo").value(1000.0));

        verify(movimentoService, times(1)).findById(1L);
    }

    @Test
    void testGetMovimentoById_NotFound() throws Exception {
        when(movimentoService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/report/movimento/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(movimentoService, times(1)).findById(1L);
    }
}




