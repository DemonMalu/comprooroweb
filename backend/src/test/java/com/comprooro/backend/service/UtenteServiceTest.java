package com.comprooro.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import com.comprooro.backend.dto.UtenteRequestDTO;
import com.comprooro.backend.dto.UtenteResponseDTO;
import com.comprooro.backend.model.Utente;
import com.comprooro.backend.repo.UtenteRepository;

@ExtendWith(MockitoExtension.class)
class UtenteServiceTest {

    @Mock
    private UtenteRepository utenteRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UtenteServiceImpl utenteService;

    private MultipartFile file;
    
    @BeforeEach
    void setUp() throws Exception {
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

        file = new MockMultipartFile(
                "documento", 
                "documento.pdf", 
                "application/pdf", 
                "contenuto del documento".getBytes()
        );
    }


    @Test
    void testRegister_Success() {
        UtenteRequestDTO requestDTO = new UtenteRequestDTO(
            "user1", "password", "Mario", "Rossi",
            LocalDate.of(1990, 1, 1), "Roma", "mario.rossi@example.com",
            null, null, "RSSMRA90A01H501Z", "Via Roma 1", "CLIENT"
        );

        when(utenteRepo.existsById("user1")).thenReturn(false);
        when(utenteRepo.existsByEmail("mario.rossi@example.com")).thenReturn(false);
        when(utenteRepo.existsByCodiceFiscale("RSSMRA90A01H501Z")).thenReturn(false);

        String result = utenteService.register(requestDTO, file);

        assertEquals("Registrazione completata.", result);
        verify(utenteRepo, times(1)).save(any(Utente.class));
    }

    @Test
    void testRegister_UsernameAlreadyExists() {
        UtenteRequestDTO requestDTO = new UtenteRequestDTO("user1", "password", "Mario", "Rossi", 
            LocalDate.of(1990, 1, 1), "Roma", "mario.rossi@example.com", null, null, "RSSMRA90A01H501Z", "Via Roma 1", "CLIENT");
        when(utenteRepo.existsById("user1")).thenReturn(true);

        String result = utenteService.register(requestDTO, file);

        assertEquals("Username già in uso.", result);
    }
    
    @Test
    void testRegister_PasswordTroppoCorta() {
        UtenteRequestDTO requestDTO = new UtenteRequestDTO(
            "user1", "123", "Mario", "Rossi",
            LocalDate.of(1990, 1, 1), "Roma", "mario.rossi@example.com",
            null, null, "RSSMRA90A01H501Z", "Via Roma 1", "CLIENT"
        );

        when(utenteRepo.existsById("user1")).thenReturn(false);

        String result = utenteService.register(requestDTO, file);

        assertEquals("Password troppo corta, deve essere di almeno 5 caratteri", result);
        verify(utenteRepo, never()).save(any());
    }

    @Test
    void testRegister_CodiceFiscaleAlreadyExists() {
        UtenteRequestDTO requestDTO = new UtenteRequestDTO(
            "user1", "password", "Mario", "Rossi",
            LocalDate.of(1990, 1, 1), "Roma", "mario.rossi@example.com",
            null, null, "RSSMRA90A01H501Z", "Via Roma 1", "CLIENT"
        );

        when(utenteRepo.existsById("user1")).thenReturn(false);
        when(utenteRepo.existsByEmail("mario.rossi@example.com")).thenReturn(false);
        when(utenteRepo.existsByCodiceFiscale("RSSMRA90A01H501Z")).thenReturn(true);

        String result = utenteService.register(requestDTO, file);

        assertEquals("Codice fiscale già in uso.", result);
        verify(utenteRepo, never()).save(any());
    }
    
    @Test
    void testRegister_EmailAlreadyExists() {
        UtenteRequestDTO requestDTO = new UtenteRequestDTO(
            "user1", "password", "Mario", "Rossi",
            LocalDate.of(1990, 1, 1), "Roma", "mario.rossi@example.com",
            null, null, "RSSMRA90A01H501Z", "Via Roma 1", "CLIENT"
        );

        when(utenteRepo.existsById("user1")).thenReturn(false);
        when(utenteRepo.existsByEmail("mario.rossi@example.com")).thenReturn(true);

        String result = utenteService.register(requestDTO, file);

        assertEquals("Email già in uso.", result);
        verify(utenteRepo, never()).save(any());
    }

    @Test
    void testRegister_DocumentoMancante() {
        UtenteRequestDTO requestDTO = new UtenteRequestDTO(
            "user1", "password", "Mario", "Rossi",
            LocalDate.of(1990, 1, 1), "Roma", "mario.rossi@example.com",
            null, null, "RSSMRA90A01H501Z", "Via Roma 1", "CLIENT"
        );

        when(utenteRepo.existsById("user1")).thenReturn(false);
        when(utenteRepo.existsByEmail("mario.rossi@example.com")).thenReturn(false);
        when(utenteRepo.existsByCodiceFiscale("RSSMRA90A01H501Z")).thenReturn(false);

        String result = utenteService.register(requestDTO, null);

        assertEquals("Documento scannerizzato obbligatorio", result);
        verify(utenteRepo, never()).save(any());
    }

    @Test
    void testRegister_NomeCognomeInizialiMinuscole() {
        UtenteRequestDTO requestDTO = new UtenteRequestDTO(
            "user1", "password", "mario", "rossi", // minuscole
            LocalDate.of(1990, 1, 1), "roma", "mario.rossi@example.com",
            null, null, "RSSMRA90A01H501Z", "via roma 1", "CLIENT"
        );

        when(utenteRepo.existsById("user1")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            utenteService.register(requestDTO, file)
        );

        assertEquals("Nome, Cognome, Città e Indirizzo devono iniziare con una lettera maiuscola.", exception.getMessage());
    }

    @Test
    void testFindByUsername_Success() {
        Utente utente = new Utente("user1", "hashedPassword", "Mario", "Rossi", LocalDate.of(1990, 1, 1), "Roma", 
            "mario.rossi@example.com", null, null, "RSSMRA90A01H501Z", "Via Roma 1", "CLIENT");
        when(utenteRepo.findById("user1")).thenReturn(Optional.of(utente));

        Optional<UtenteResponseDTO> result = utenteService.findByUsername("user1");

        assertTrue(result.isPresent());
        assertEquals("1990-01-01", result.get().getDataNascita().toString());
    }

    @Test
    void testFindByUsername_NotFound() {
        when(utenteRepo.findById("user1")).thenReturn(Optional.empty());

        Optional<UtenteResponseDTO> result = utenteService.findByUsername("user1");

        assertFalse(result.isPresent());
    }

    @Test
    void testFindAllUsers() {
        List<Utente> utenti = Arrays.asList(
            new Utente("user1", "hashedPassword", "Mario", "Rossi", LocalDate.of(1990, 1, 1), "Roma", 
                "mario.rossi@example.com", null, null, "RSSMRA90A01H501Z", "Via Roma 1", "CLIENT"),
            new Utente("user2", "hashedPassword", "Luca", "Bianchi", LocalDate.of(1985, 2, 2), "Milano", 
                "luca.bianchi@example.com", null, null, "BNCLCU85B02F205X", "Via Milano 2", "OPERATOR")
        );
        when(utenteRepo.findAll()).thenReturn(utenti);

        List<UtenteResponseDTO> result = utenteService.findAllUsers();

        assertEquals(2, result.size());
        assertEquals("1990-01-01", result.get(0).getDataNascita().toString());
        assertEquals("1985-02-02", result.get(1).getDataNascita().toString());
    }
}
