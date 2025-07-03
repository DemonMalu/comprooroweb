package com.comprooro.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

import com.comprooro.backend.dto.OperazioneRequestDTO;
import com.comprooro.backend.dto.OperazioneResponseDTO;
import com.comprooro.backend.model.Operazione;
import com.comprooro.backend.model.Utente;
import com.comprooro.backend.repo.OperazioneRepository;
import com.comprooro.backend.repo.UtenteRepository;

@ExtendWith(MockitoExtension.class)
class OperazioneServiceTest {

    @Mock
    private OperazioneRepository operazioneRepo;

    @Mock
    private UtenteRepository utenteRepo;

    @InjectMocks
    private OperazioneServiceImpl operazioneService;

    private OperazioneRequestDTO operazioneRequestDTO;
    private Operazione operazione;
    private Utente utente;

    @BeforeEach
    void setUp() {
        utente = new Utente();
        utente.setUsername("testuser");

        operazioneRequestDTO = new OperazioneRequestDTO();
        operazioneRequestDTO.setDescrizione("Vendita oro");
        operazioneRequestDTO.setTipo(1);
        operazioneRequestDTO.setImporto(BigDecimal.valueOf(1500));
        operazioneRequestDTO.setData(LocalDate.of(2025, 2, 28));
        operazioneRequestDTO.setUsername("testuser");

        operazione = new Operazione();
        operazione.setIdOperazione(1L);
        operazione.setDescrizione("Vendita oro");
        operazione.setTipo(1);
        operazione.setImporto(BigDecimal.valueOf(1500));
        operazione.setData(LocalDate.of(2025, 2, 28));
        operazione.setUtente(utente);
    }

    @Test
    void testRegister() {
        when(utenteRepo.findById("testuser")).thenReturn(Optional.of(utente));
        when(operazioneRepo.save(any(Operazione.class))).thenReturn(operazione);

        OperazioneResponseDTO responseDTO = operazioneService.register(operazioneRequestDTO);

        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getIdOperazione());
        assertEquals("Vendita oro", responseDTO.getDescrizione());
        assertEquals(1, responseDTO.getTipo());
        assertEquals(BigDecimal.valueOf(1500), responseDTO.getImporto());
        assertEquals("2025-02-28", responseDTO.getData().toString());
        assertEquals("testuser", responseDTO.getUsername());

        verify(utenteRepo, times(1)).findById("testuser");
        verify(operazioneRepo, times(1)).save(any(Operazione.class));
    }

    @Test
    void testRegister_UtenteNonTrovato() {
        when(utenteRepo.findById("testuser")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            operazioneService.register(operazioneRequestDTO);
        });

        assertEquals("Utente non trovato", exception.getMessage());

        verify(utenteRepo, times(1)).findById("testuser");
        verify(operazioneRepo, never()).save(any(Operazione.class));
    }
    
    @Test
    void testRegister_ImportoNonValido() {
        operazioneRequestDTO.setImporto(BigDecimal.ZERO);

        when(utenteRepo.findById("testuser")).thenReturn(Optional.of(utente));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            operazioneService.register(operazioneRequestDTO);
        });

        assertEquals("L'importo deve essere maggiore di zero.", exception.getMessage());
        verify(utenteRepo, times(1)).findById("testuser");
        verify(operazioneRepo, never()).save(any(Operazione.class));
    }


    @Test
    void testFindAll() {
        when(operazioneRepo.findAll()).thenReturn(Arrays.asList(operazione));

        List<OperazioneResponseDTO> operazioni = operazioneService.findAll();

        assertNotNull(operazioni);
        assertEquals(1, operazioni.size());
        assertEquals("Vendita oro", operazioni.get(0).getDescrizione());

        verify(operazioneRepo, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(operazioneRepo.findById(1L)).thenReturn(Optional.of(operazione));

        Optional<OperazioneResponseDTO> responseDTO = operazioneService.findById(1L);

        assertTrue(responseDTO.isPresent());
        assertEquals("Vendita oro", responseDTO.get().getDescrizione());

        verify(operazioneRepo, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(operazioneRepo.findById(1L)).thenReturn(Optional.empty());

        Optional<OperazioneResponseDTO> responseDTO = operazioneService.findById(1L);

        assertFalse(responseDTO.isPresent());

        verify(operazioneRepo, times(1)).findById(1L);
    }
}
