package com.comprooro.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import com.comprooro.backend.dto.ArticoloRequestDTO;
import com.comprooro.backend.dto.ArticoloResponseDTO;
import com.comprooro.backend.service.ArticoloService;
import com.comprooro.backend.service.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ArticoloController.class)
@ExtendWith(MockitoExtension.class)
class ArticoloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ArticoloService articoloService;

    @Mock
    private ReportService reportService;

    private ArticoloRequestDTO articoloRequestDTO;
    private ArticoloResponseDTO articoloResponseDTO;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        // Creazione dell'ArticoloRequestDTO
        articoloRequestDTO = new ArticoloRequestDTO();
        articoloRequestDTO.setNome("Bracciale");
        articoloRequestDTO.setDescrizione("Bracciale d'oro 18k");
        articoloRequestDTO.setGrammi(BigDecimal.valueOf(10.5));
        articoloRequestDTO.setCaratura("18");
        articoloRequestDTO.setIdMovimento(1L);

        // Creazione dell'ArticoloResponseDTO
        articoloResponseDTO = new ArticoloResponseDTO();
        articoloResponseDTO.setIdArticolo(1L);
        articoloResponseDTO.setNome("Bracciale");
        articoloResponseDTO.setDescrizione("Bracciale d'oro 18k");
        articoloResponseDTO.setGrammi(BigDecimal.valueOf(10.5));
        articoloResponseDTO.setCaratura("18");
        articoloResponseDTO.setIdMovimento(1L);
    }

    @Test
    void testRegister() throws Exception {
        // Simula la generazione di un PDF
        byte[] pdfBytes = "Test PDF".getBytes();

        // Mock dei servizi
        when(articoloService.register(any(ArticoloRequestDTO.class), any(MultipartFile.class), any(MultipartFile.class)))
            .thenReturn(articoloResponseDTO);
        when(reportService.generaCartellinoArticoloPDF(any(ArticoloResponseDTO.class)))
            .thenReturn(pdfBytes);

        // Simula l'invio di due file tramite MultipartFile
        MockMultipartFile foto1 = new MockMultipartFile("foto1", "foto1.jpg", MediaType.IMAGE_JPEG_VALUE, "Test image 1".getBytes());
        MockMultipartFile foto2 = new MockMultipartFile("foto2", "foto2.jpg", MediaType.IMAGE_JPEG_VALUE, "Test image 2".getBytes());

        mockMvc.perform(multipart("/api/home/operatore/register-articolo")
                .file(foto1)
                .file(foto2)
                .param("articolo", objectMapper.writeValueAsString(articoloRequestDTO))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=cartellino_articolo_1.pdf"))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(content().bytes(pdfBytes));

        // Verifica che i metodi siano stati chiamati
        verify(articoloService, times(1)).register(any(ArticoloRequestDTO.class), any(MultipartFile.class), any(MultipartFile.class));
        verify(reportService, times(1)).generaCartellinoArticoloPDF(any(ArticoloResponseDTO.class));
    }

    @Test
    void testFindAll() throws Exception {
        List<ArticoloResponseDTO> articoli = Arrays.asList(articoloResponseDTO);
        when(articoloService.findAll()).thenReturn(articoli);

        mockMvc.perform(get("/api/report/all-articoli")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].idArticolo").value(1))
                .andExpect(jsonPath("$[0].nome").value("Bracciale"));

        verify(articoloService, times(1)).findAll();
    }

    @Test
    void testFindById() throws Exception {
        when(articoloService.findById(1L)).thenReturn(articoloResponseDTO);

        mockMvc.perform(get("/api/report/articolo/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idArticolo").value(1))
                .andExpect(jsonPath("$.nome").value("Bracciale"));

        verify(articoloService, times(1)).findById(1L);
    }
}
