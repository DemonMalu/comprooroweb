package com.comprooro.backend.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.comprooro.backend.dto.OperazioneResponseDTO;
import com.comprooro.backend.dto.OperazioneRequestDTO;
import com.comprooro.backend.service.OperazioneService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class OperazioneController {

    private static final Logger logger = LoggerFactory.getLogger(OperazioneController.class);
    private final OperazioneService operazioneService;

    @Autowired
    public OperazioneController(OperazioneService operazioneService) {
        this.operazioneService = operazioneService;
    }

    @PostMapping("/home/amministratore/register-operazione")
    public ResponseEntity<OperazioneResponseDTO> register(@Valid @RequestBody OperazioneRequestDTO requestDTO) {
        logger.info("Registrazione di una nuova operazione: {}", requestDTO.getDescrizione());
        OperazioneResponseDTO response = operazioneService.register(requestDTO);
        logger.info("Operazione registrata con ID: {}", response.getIdOperazione());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/report/all-operazioni")
    public ResponseEntity<List<OperazioneResponseDTO>> getAll() {
        logger.info("Recupero di tutte le operazioni.");
        List<OperazioneResponseDTO> operazioni = operazioneService.findAll();
        logger.info("Recuperate {} operazioni.", operazioni.size());
        return ResponseEntity.ok(operazioni);
    }

    @GetMapping("/report/operazione/{id}")
    public ResponseEntity<OperazioneResponseDTO> getOperazioneById(@PathVariable Long id) {
        logger.info("Ricerca operazione con ID: {}", id);
        return operazioneService.findById(id)
                .map(operazione -> {
                    logger.info("Operazione trovata: {}", operazione.getDescrizione());
                    return ResponseEntity.ok(operazione);
                })
                .orElseGet(() -> {
                    logger.warn("Operazione non trovata con ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }
}
