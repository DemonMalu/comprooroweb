package com.comprooro.backend.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.comprooro.backend.dto.UtenteRequestDTO;
import com.comprooro.backend.dto.UtenteResponseDTO;
import com.comprooro.backend.service.UtenteService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class UtenteController {

    private static final Logger logger = LoggerFactory.getLogger(UtenteController.class);
    private final UtenteService utenteService;

    @Autowired
    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @PostMapping("/home/operatore/register-cliente")
    public ResponseEntity<String> register(
            @RequestPart("cliente") @Valid UtenteRequestDTO requestDTO,
            @RequestPart(value = "documento", required = true) MultipartFile documento) {

        logger.info("Registrazione nuovo cliente: {}", requestDTO.getUsername());

        String response = utenteService.register(requestDTO, documento);

        logger.info("Registrazione completata per: {}", requestDTO.getUsername());

        return ResponseEntity.ok(response);
    }


    @GetMapping("/report/ricerca-utente/{username}")
    public ResponseEntity<UtenteResponseDTO> getUserByUsername(@PathVariable String username) {
        logger.info("Ricerca utente per username: {}", username);
        return utenteService.findByUsername(username)
                .map(user -> {
                    logger.info("Utente trovato: {}", username);
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> {
                    logger.warn("Utente non trovato: {}", username);
                    return ResponseEntity.notFound().build();
                });
    }
    
    @GetMapping("/report/ricerca-cliente/{username}")
    public ResponseEntity<UtenteResponseDTO> getClientByUsername(@PathVariable String username) {
        logger.info("Ricerca cliente per username: {}", username);
        return utenteService.findClienteByUsername(username)
                .map(user -> {
                    logger.info("Cliente trovato: {}", username);
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> {
                    logger.warn("Cliente non trovato: {}", username);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/home/cliente/profilo")
    public ResponseEntity<UtenteResponseDTO> visualizzaProfilo(HttpSession session, Authentication authentication) {
    	if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Tentativo di accesso a /home/cliente/profilo senza sessione valida.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    	
    	String username = authentication.getName();
        logger.info("Visualizzazione profilo cliente: {}", username);
        
        if (session.isNew()) {
            logger.warn("Nuova sessione creata per il'cliente{} - possibile scadenza della sessione precedente.", username);
        } else {
            logger.info("Sessione attiva per il'cliente {} - ID sessione: {}", username, session.getId());
        }
        
        return utenteService.findByUsername(username)
                .map(user -> {
                    logger.info("Profilo cliente trovato: {}", username);
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> {
                    logger.warn("Profilo cliente non trovato: {}", username);
                    return ResponseEntity.notFound().build();
                });
    }
    
    @GetMapping("/home/amministratore/profilo")
    public ResponseEntity<UtenteResponseDTO> visualizzaProfiloAdmin(HttpSession session, Authentication authentication) {
    	if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Tentativo di accesso a /home/amministratore/profilo senza sessione valida.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    	
    	String username = authentication.getName();
        logger.info("Visualizzazione profilo admin: {}", username);
        
        if (session.isNew()) {
            logger.warn("Nuova sessione creata per l'admin {} - possibile scadenza della sessione precedente.", username);
        } else {
            logger.info("Sessione attiva per l'admin {} - ID sessione: {}", username, session.getId());
        }
        
        return utenteService.findByUsername(username)
                .map(user -> {
                    logger.info("Profilo admin trovato: {}", username);
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> {
                    logger.warn("Profilo admin non trovato: {}", username);
                    return ResponseEntity.notFound().build();
                });
    }
    
    @GetMapping("/home/operatore/profilo")
    public ResponseEntity<UtenteResponseDTO> visualizzaProfiloOperatore(HttpSession session, Authentication authentication) {
    	if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Tentativo di accesso a /home/operatore/profilo senza sessione valida.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    	
    	String username = authentication.getName();
        logger.info("Visualizzazione profilo operatore: {}", username);
        
        if (session.isNew()) {
            logger.warn("Nuova sessione creata per l'operatore {} - possibile scadenza della sessione precedente.", username);
        } else {
            logger.info("Sessione attiva per l'operatore {} - ID sessione: {}", username, session.getId());
        }
        
        return utenteService.findByUsername(username)
                .map(user -> {
                    logger.info("Profilo operatore trovato: {}", username);
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> {
                    logger.warn("Profilo operatore non trovato: {}", username);
                    return ResponseEntity.notFound().build();
                });
    }
    
    @GetMapping("/report/profilo")
    public ResponseEntity<UtenteResponseDTO> visualizzaProfiloReport(HttpSession session, Authentication authentication) {
    	if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Tentativo di accesso a /report/profilo senza sessione valida.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    	
    	String username = authentication.getName();
        logger.info("Visualizzazione profilo utente: {}", username);
        
        if (session.isNew()) {
            logger.warn("Nuova sessione creata per l'utente {} - possibile scadenza della sessione precedente.", username);
        } else {
            logger.info("Sessione attiva per l'utente {} - ID sessione: {}", username, session.getId());
        }
        
        return utenteService.findByUsername(username)
                .map(user -> {
                    logger.info("Profilo utente trovato: {}", username);
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> {
                    logger.warn("Profilo utente non trovato: {}", username);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/report/all-utenti")
    public ResponseEntity<List<UtenteResponseDTO>> getAllUsers() {
        logger.info("Recupero di tutti gli utenti.");
        List<UtenteResponseDTO> utenti = utenteService.findAllUsers();
        logger.info("Trovati {} utenti.", utenti.size());
        return ResponseEntity.ok(utenti);
    }
    
    @GetMapping("/report/exist-username/{username}")
    public ResponseEntity<Boolean> verificaUsername(@PathVariable String username) {
        boolean exists = utenteService.existByUsername(username);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/report/exist-email/{email}")
    public ResponseEntity<Boolean> verificaEmail(@PathVariable String email) {
        boolean exists = utenteService.existByEmail(email);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/report/exist-codice/{codiceFiscale}")
    public ResponseEntity<Boolean> verificaCodiceFiscale(@PathVariable String codiceFiscale) {
        boolean exists = utenteService.existByCodiceFiscale(codiceFiscale);
        return ResponseEntity.ok(exists);
    }

}




