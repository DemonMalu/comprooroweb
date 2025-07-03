package com.comprooro.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.comprooro.backend.dto.MovimentoRequestDTO;
import com.comprooro.backend.dto.MovimentoResponseDTO;
import com.comprooro.backend.model.Movimento;
import com.comprooro.backend.model.Utente;
import com.comprooro.backend.repo.MovimentoRepository;
import com.comprooro.backend.repo.UtenteRepository;

@ExtendWith(MockitoExtension.class)
class MovimentoServiceTest {

    @Mock
    private MovimentoRepository movimentoRepo;

    @Mock
    private UtenteRepository utenteRepo;

    @InjectMocks
    private MovimentoServiceImpl movimentoService;

    private Movimento movimento;
    private Utente utente;
    private MovimentoRequestDTO movimentoRequestDTO;
    private MultipartFile file;

    @BeforeEach
    void setUp() throws IOException {
        utente = new Utente();
        utente.setUsername("testuser");

        file = new MockMultipartFile("assegno", "assegno.pdf", "application/pdf", "contenuto finto".getBytes());

        movimento = new Movimento();
        movimento.setIdMovimento(1L);
        movimento.setData(LocalDate.parse("2024-02-20"));
        movimento.setModalitaPagamento("Bonifico");
        movimento.setImporto(BigDecimal.valueOf(1000.0));
        movimento.setAssegnoScannerizzato(file.getBytes());
        movimento.setContentType(file.getContentType());
        movimento.setUtente(utente);

        movimentoRequestDTO = new MovimentoRequestDTO();
        movimentoRequestDTO.setUsername("testuser");
        movimentoRequestDTO.setData(LocalDate.parse("2024-02-20"));
        movimentoRequestDTO.setModalitaPagamento("Bonifico");
        movimentoRequestDTO.setImporto(BigDecimal.valueOf(1000.0));
    }

    @Test
    void testRegister() {
        when(utenteRepo.findById("testuser")).thenReturn(Optional.of(utente));
        when(movimentoRepo.save(any(Movimento.class))).thenReturn(movimento);

        MovimentoResponseDTO response = movimentoService.register(movimentoRequestDTO, file);

        assertNotNull(response);
        assertEquals(1L, response.getIdMovimento());
        assertEquals("2024-02-20", response.getData().toString());
        assertEquals("Bonifico", response.getModalitaPagamento());
        assertEquals(BigDecimal.valueOf(1000.0), response.getImporto());
        assertEquals("application/pdf", response.getContentType());
    }

    @Test
    void testFindAll() {
        when(movimentoRepo.findAll()).thenReturn(Arrays.asList(movimento));

        List<MovimentoResponseDTO> result = movimentoService.findAll();

        assertEquals(1, result.size());
        assertEquals("2024-02-20", result.get(0).getData().toString());
        assertEquals(BigDecimal.valueOf(1000.0), result.get(0).getImporto());
    }

    @Test
    void testFindById() {
        when(movimentoRepo.findById(1L)).thenReturn(Optional.of(movimento));

        Optional<MovimentoResponseDTO> result = movimentoService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("2024-02-20", result.get().getData().toString());
        assertEquals(BigDecimal.valueOf(1000.0), result.get().getImporto());
    }

    @Test
    void testFindByUsername() {
        when(movimentoRepo.findByUtente_Username("testuser")).thenReturn(Arrays.asList(movimento));

        List<MovimentoResponseDTO> result = movimentoService.findByUsername("testuser");

        assertEquals(1, result.size());
        assertEquals("2024-02-20", result.get(0).getData().toString());
        assertEquals(BigDecimal.valueOf(1000.0), result.get(0).getImporto());
    }
}
