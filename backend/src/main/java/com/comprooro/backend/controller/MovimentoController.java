package com.comprooro.backend.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.comprooro.backend.dto.MovimentoRequestDTO;
import com.comprooro.backend.dto.MovimentoResponseDTO;
import com.comprooro.backend.service.MovimentoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class MovimentoController {

    private static final Logger logger = LoggerFactory.getLogger(MovimentoController.class);
    private final MovimentoService movimentoService;

    @Autowired
    public MovimentoController(MovimentoService movimentoService) {
        this.movimentoService = movimentoService;
    }

    @PostMapping("/home/operatore/register-movimento")
    public ResponseEntity<MovimentoResponseDTO> register(
            @RequestPart("movimento") @Valid MovimentoRequestDTO requestDTO,
            @RequestPart(value = "assegno", required = true) MultipartFile assegno) {

        logger.info("Registrazione nuovo movimento per cliente: {}", requestDTO.getUsername());

        MovimentoResponseDTO response = movimentoService.register(requestDTO, assegno);

        logger.info("Movimento registrato con ID: {}", response.getIdMovimento());

        return ResponseEntity.ok(response);
    }


    @GetMapping("/home/cliente/storico")
    public ResponseEntity<List<MovimentoResponseDTO>> getMovimentiDelCliente(Authentication authentication) {
        String username = authentication.getName();
        logger.info("Richiesta storico movimenti per il cliente: {}", username);
        List<MovimentoResponseDTO> movimenti = movimentoService.findByUsername(username);
        logger.info("Trovati {} movimenti per il cliente: {}", movimenti.size(), username);
        return ResponseEntity.ok(movimenti);
    }

    @GetMapping("/report/all-movimenti")
    public ResponseEntity<List<MovimentoResponseDTO>> getAll() {
        logger.info("Recupero di tutti i movimenti.");
        List<MovimentoResponseDTO> movimenti = movimentoService.findAll();
        logger.info("Recuperati {} movimenti.", movimenti.size());
        return ResponseEntity.ok(movimenti);
    }

    @GetMapping("/report/movimento/{id}")
    public ResponseEntity<MovimentoResponseDTO> getMovimentoById(@PathVariable Long id) {
        logger.info("Ricerca movimento con ID: {}", id);
        return movimentoService.findById(id)
                .map(movimento -> {
                    logger.info("Movimento trovato con ID: {}", id);
                    return ResponseEntity.ok(movimento);
                })
                .orElseGet(() -> {
                    logger.warn("Movimento non trovato con ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }
}




