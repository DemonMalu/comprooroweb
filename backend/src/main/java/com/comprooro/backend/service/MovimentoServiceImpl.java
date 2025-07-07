package com.comprooro.backend.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.comprooro.backend.dto.MovimentoRequestDTO;
import com.comprooro.backend.dto.MovimentoResponseDTO;
import com.comprooro.backend.model.Movimento;
import com.comprooro.backend.model.Utente;
import com.comprooro.backend.repo.MovimentoRepository;
import com.comprooro.backend.repo.UtenteRepository;

import jakarta.transaction.Transactional;

@Service
public class MovimentoServiceImpl implements MovimentoService {

    private static final Logger logger = LoggerFactory.getLogger(MovimentoServiceImpl.class);

    private final MovimentoRepository movimentoRepo;
    private final UtenteRepository utenteRepo;

    @Autowired
    public MovimentoServiceImpl(MovimentoRepository movimentoRepo, UtenteRepository utenteRepo) {
        this.movimentoRepo = movimentoRepo;
        this.utenteRepo = utenteRepo;
    }

    private MovimentoResponseDTO mapToResponseDTO(Movimento movimento) {
        MovimentoResponseDTO dto = new MovimentoResponseDTO();
        dto.setIdMovimento(movimento.getIdMovimento());
        dto.setData(movimento.getData());
        dto.setModalitaPagamento(movimento.getModalitaPagamento());
        dto.setImporto(movimento.getImporto());
        dto.setAssegnoScannerizzato(movimento.getAssegnoScannerizzato());
        dto.setContentType(movimento.getContentType());
        dto.setUsername(movimento.getUtente().getUsername());
        return dto;
    }

    @Override
    @Transactional
    public MovimentoResponseDTO register(MovimentoRequestDTO requestDTO, MultipartFile assegno) {
        logger.info("Registrazione movimento per username: {}", requestDTO.getUsername());

        Utente utente = utenteRepo.findById(requestDTO.getUsername())
                .orElseThrow(() -> {
                    logger.warn("Utente non trovato: {}", requestDTO.getUsername());
                    return new IllegalArgumentException("Utente non trovato");
                });
        
        if (requestDTO.getImporto().compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("Importo non valido: {}", requestDTO.getImporto());
            throw new IllegalArgumentException("L'importo deve essere maggiore di zero.");
        }
        
        byte[] assegnoBytes = null;
        String contentType = null;

        if (assegno != null && !assegno.isEmpty()) {
            try {
                assegnoBytes = assegno.getBytes();
                contentType = assegno.getContentType();
            } catch (IOException e) {
                logger.error("Errore durante la lettura dell'assegno: {}", e.getMessage());
                throw new RuntimeException("Errore durante il caricamento dell'assegno.", e);
            }
        }

        Movimento movimento = new Movimento();
        movimento.setData(requestDTO.getData());
        movimento.setModalitaPagamento(requestDTO.getModalitaPagamento());
        movimento.setImporto(requestDTO.getImporto());
        movimento.setAssegnoScannerizzato(assegnoBytes);
        movimento.setContentType(contentType);
        movimento.setUtente(utente);

        Movimento savedMovimento = movimentoRepo.save(movimento);
        logger.info("Movimento registrato con successo: ID {}", savedMovimento.getIdMovimento());

        return mapToResponseDTO(savedMovimento);
    }


    @Override
    public List<MovimentoResponseDTO> findAll() {
        logger.info("Recupero di tutti i movimenti.");
        List<MovimentoResponseDTO> movimenti = movimentoRepo.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        logger.info("Trovati {} movimenti.", movimenti.size());
        return movimenti;
    }

    @Override
    public Optional<MovimentoResponseDTO> findById(Long id) {
        logger.info("Ricerca movimento per ID: {}", id);
        Optional<MovimentoResponseDTO> movimentoOpt = movimentoRepo.findById(id)
                .map(this::mapToResponseDTO);

        if (movimentoOpt.isPresent()) {
            logger.info("Movimento trovato: ID {}", id);
        } else {
            logger.warn("Movimento non trovato: ID {}", id);
        }

        return movimentoOpt;
    }

    @Override
    public List<MovimentoResponseDTO> findByUsername(String username) {
        logger.info("Ricerca movimenti per username: {}", username);
        List<MovimentoResponseDTO> movimenti = movimentoRepo.findByUtente_Username(username).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        logger.info("Trovati {} movimenti per username: {}", movimenti.size(), username);
        return movimenti;
    }
}
