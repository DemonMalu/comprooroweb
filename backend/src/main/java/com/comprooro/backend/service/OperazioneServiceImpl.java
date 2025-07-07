package com.comprooro.backend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comprooro.backend.dto.OperazioneResponseDTO;
import com.comprooro.backend.dto.OperazioneRequestDTO;
import com.comprooro.backend.model.Operazione;
import com.comprooro.backend.model.Utente;
import com.comprooro.backend.repo.OperazioneRepository;
import com.comprooro.backend.repo.UtenteRepository;

import jakarta.transaction.Transactional;

@Service
public class OperazioneServiceImpl implements OperazioneService {

    private static final Logger logger = LoggerFactory.getLogger(OperazioneServiceImpl.class);

    private final OperazioneRepository operazioneRepo;
    private final UtenteRepository utenteRepo;

    @Autowired
    public OperazioneServiceImpl(OperazioneRepository operazioneRepo, UtenteRepository utenteRepo) {
        this.operazioneRepo = operazioneRepo;
        this.utenteRepo = utenteRepo;
    }

    private OperazioneResponseDTO mapToResponseDTO(Operazione operazione) {
        OperazioneResponseDTO dto = new OperazioneResponseDTO();
        dto.setIdOperazione(operazione.getIdOperazione());
        dto.setDescrizione(operazione.getDescrizione());
        dto.setTipo(operazione.getTipo());
        dto.setImporto(operazione.getImporto());
        dto.setData(operazione.getData());
        dto.setUsername(operazione.getUtente().getUsername());
        return dto;
    }

    @Override
    @Transactional
    public OperazioneResponseDTO register(OperazioneRequestDTO requestDTO) {
        logger.info("Registrazione operazione: {}", requestDTO.getDescrizione());

        Utente utente = utenteRepo.findById(requestDTO.getUsername())
                .orElseThrow(() -> {
                    logger.warn("Utente non trovato: {}", requestDTO.getUsername());
                    return new IllegalArgumentException("Utente non trovato");
                });
        
        if (requestDTO.getImporto().compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("Importo non valido: {}", requestDTO.getImporto());
            throw new IllegalArgumentException("L'importo deve essere maggiore di zero.");
        }

        Operazione operazione = new Operazione();
        operazione.setDescrizione(requestDTO.getDescrizione());
        operazione.setTipo(requestDTO.getTipo());
        operazione.setImporto(requestDTO.getImporto());
        operazione.setData(requestDTO.getData());
        operazione.setUtente(utente);

        Operazione savedOperazione = operazioneRepo.save(operazione);
        logger.info("Operazione registrata con successo: ID {}", savedOperazione.getIdOperazione());

        return mapToResponseDTO(savedOperazione);
    }

    @Override
    public List<OperazioneResponseDTO> findAll() {
        logger.info("Recupero di tutte le operazioni.");
        List<OperazioneResponseDTO> operazioni = operazioneRepo.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        logger.info("Trovate {} operazioni.", operazioni.size());
        return operazioni;
    }

    @Override
    public Optional<OperazioneResponseDTO> findById(Long id) {
        logger.info("Ricerca operazione per ID: {}", id);
        Optional<OperazioneResponseDTO> operazioneOpt = operazioneRepo.findById(id)
                .map(this::mapToResponseDTO);

        if (operazioneOpt.isPresent()) {
            logger.info("Operazione trovata: ID {}", id);
        } else {
            logger.warn("Operazione non trovata: ID {}", id);
        }

        return operazioneOpt;
    }
}
