package com.comprooro.backend.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.comprooro.backend.dto.*;
import com.comprooro.backend.service.*;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @InjectMocks
    private ReportController reportController;

    @Mock
    private ReportService reportService;

    @Mock
    private MovimentoService movimentoService;

    @Mock
    private ArticoloService articoloService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGenerateDichiarazioneVendita_Success() {
        Long idMovimento = 1L;
        MovimentoResponseDTO movimento = new MovimentoResponseDTO();
        movimento.setIdMovimento(idMovimento);
        List<ArticoloResponseDTO> articoli = List.of(new ArticoloResponseDTO());

        when(movimentoService.findById(idMovimento)).thenReturn(Optional.of(movimento));
        when(articoloService.findByMovimento(idMovimento)).thenReturn(articoli);
        when(reportService.generaDichiarazioneVenditaPDF(movimento, articoli)).thenReturn(new byte[]{1, 2, 3});

        ResponseEntity<byte[]> response = reportController.generateDichiarazioneVendita(idMovimento);
        
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void testGenerateDichiarazioneVendita_MovimentoNotFound() {
        Long idMovimento = 2L;
        when(movimentoService.findById(idMovimento)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> 
            reportController.generateDichiarazioneVendita(idMovimento)
        );

        assertEquals("Movimento non trovato con ID: " + idMovimento, exception.getMessage());
    }

    @Test
    void testGenerateDichiarazioneVendita_NoArticoli() {
        Long idMovimento = 3L;
        MovimentoResponseDTO movimento = new MovimentoResponseDTO();
        movimento.setIdMovimento(idMovimento);

        when(movimentoService.findById(idMovimento)).thenReturn(Optional.of(movimento));
        when(articoloService.findByMovimento(idMovimento)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(RuntimeException.class, () -> 
            reportController.generateDichiarazioneVendita(idMovimento)
        );

        assertEquals("Nessun articolo associato al movimento con ID: " + idMovimento, exception.getMessage());
    }

    @Test
    void testGetBilancio_Success() {
        String data = LocalDate.now().toString();
        
        BigDecimal entrate = BigDecimal.valueOf(1000);
        BigDecimal uscite = BigDecimal.valueOf(500);
        BigDecimal rimanenze = BigDecimal.valueOf(500);
        
        BilancioResponseDTO bilancio = new BilancioResponseDTO(entrate, uscite, rimanenze);

        when(reportService.calcolaBilancio(any(LocalDate.class))).thenReturn(bilancio);

        ResponseEntity<BilancioResponseDTO> response = reportController.getBilancio(data);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(entrate, response.getBody().getTotaleEntrate());
        assertEquals(uscite, response.getBody().getTotaleUscite());
        assertEquals(rimanenze, response.getBody().getTotaleRimanenze());
    }

    @Test
    void testGetBilancio_InvalidDateFormat() {
        String invalidDate = "2024-02-29";

        ResponseEntity<BilancioResponseDTO> response = reportController.getBilancio(invalidDate);
        
        assertEquals(400, response.getStatusCode().value());
        assertNull(response.getBody());
    }
}
