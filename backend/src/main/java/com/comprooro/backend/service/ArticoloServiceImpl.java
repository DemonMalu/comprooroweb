package com.comprooro.backend.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.comprooro.backend.dto.ArticoloRequestDTO;
import com.comprooro.backend.dto.ArticoloResponseDTO;
import com.comprooro.backend.model.Articolo;
import com.comprooro.backend.model.Movimento;
import com.comprooro.backend.repo.ArticoloRepository;
import com.comprooro.backend.repo.MovimentoRepository;

import jakarta.transaction.Transactional;

@Service
public class ArticoloServiceImpl implements ArticoloService {

    private static final Logger logger = LoggerFactory.getLogger(ArticoloServiceImpl.class);

    private final ArticoloRepository articoloRepo;
    private final MovimentoRepository movimentoRepo;

    @Autowired
    public ArticoloServiceImpl(ArticoloRepository articoloRepo, MovimentoRepository movimentoRepo) {
        this.articoloRepo = articoloRepo;
        this.movimentoRepo = movimentoRepo;
    }
    
    private boolean startsWithUppercase(String value) {
        return value != null && !value.isEmpty() && Character.isUpperCase(value.charAt(0));
    }

    private ArticoloResponseDTO mapToResponseDTO(Articolo articolo) {
        ArticoloResponseDTO dto = new ArticoloResponseDTO();
        dto.setIdArticolo(articolo.getIdArticolo());
        dto.setNome(articolo.getNome());
        dto.setDescrizione(articolo.getDescrizione());
        dto.setGrammi(articolo.getGrammi());
        dto.setCaratura(articolo.getCaratura());
        dto.setFoto1(articolo.getFoto1());
        dto.setContentType(articolo.getContentType());
        dto.setFoto2(articolo.getFoto2());
        dto.setContentType2(articolo.getContentType2());
        dto.setIdMovimento(articolo.getMovimento().getIdMovimento());
        return dto;
    }

    @Override
    @Transactional
    public ArticoloResponseDTO register(ArticoloRequestDTO requestDTO, MultipartFile foto1, MultipartFile foto2) {
        logger.info("Registrazione articolo: {}", requestDTO.getNome());

        Movimento movimento = movimentoRepo.findById(requestDTO.getIdMovimento())
                .orElseThrow(() -> {
                    logger.warn("Movimento non trovato: ID {}", requestDTO.getIdMovimento());
                    return new IllegalArgumentException("Movimento non trovato");
                });
        
        if (!startsWithUppercase(requestDTO.getNome()) || 
        	    (requestDTO.getDescrizione() != null && !requestDTO.getDescrizione().isEmpty() && !startsWithUppercase(requestDTO.getDescrizione()))) {
        	    throw new IllegalArgumentException("Nome e Descrizione devono iniziare con una lettera maiuscola.");
        	}

        
        if (requestDTO.getGrammi().compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("Grammi non validi: {}", requestDTO.getGrammi());
            throw new IllegalArgumentException("I grammi devono essere maggiori di zero.");
        }

        byte[] foto1Bytes = null;
        String contentType = null;
        if (foto1 != null && !foto1.isEmpty()) {
            try {
                foto1Bytes = foto1.getBytes();
                contentType = foto1.getContentType();
                logger.info("Foto1 caricata: {} ({} bytes)", foto1.getOriginalFilename(), foto1Bytes.length);
            } catch (IOException e) {
                logger.error("Errore nella lettura di foto1", e);
                throw new RuntimeException("Errore durante la lettura di foto1", e);
            }
        }

        byte[] foto2Bytes = null;
        String contentType2 = null;
        if (foto2 != null && !foto2.isEmpty()) {
            try {
                foto2Bytes = foto2.getBytes();
                contentType2 = foto2.getContentType();
                logger.info("Foto2 caricata: {} ({} bytes)", foto2.getOriginalFilename(), foto2Bytes.length);
            } catch (IOException e) {
                logger.error("Errore nella lettura di foto2", e);
                throw new RuntimeException("Errore durante la lettura di foto2", e);
            }
        }

        Articolo articolo = new Articolo();
        articolo.setNome(requestDTO.getNome());
        articolo.setDescrizione(requestDTO.getDescrizione());
        articolo.setGrammi(requestDTO.getGrammi());
        articolo.setCaratura(requestDTO.getCaratura());
        articolo.setFoto1(foto1Bytes);
        articolo.setContentType(contentType);
        articolo.setFoto2(foto2Bytes);
        articolo.setContentType2(contentType2);
        articolo.setMovimento(movimento);

        Articolo savedArticolo = articoloRepo.save(articolo);
        logger.info("Articolo registrato con successo: ID {}", savedArticolo.getIdArticolo());

        return mapToResponseDTO(savedArticolo);
    }


    @Override
    public List<ArticoloResponseDTO> findAll() {
        logger.info("Recupero di tutti gli articoli.");
        List<ArticoloResponseDTO> articoli = articoloRepo.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        logger.info("Trovati {} articoli.", articoli.size());
        return articoli;
    }

    @Override
    public ArticoloResponseDTO findById(Long id) {
        logger.info("Ricerca articolo per ID: {}", id);
        Articolo articolo = articoloRepo.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Articolo non trovato: ID {}", id);
                    return new IllegalArgumentException("Articolo non trovato");
                });

        logger.info("Articolo trovato: ID {}", id);
        return mapToResponseDTO(articolo);
    }

    @Override
    public List<ArticoloResponseDTO> findByMovimento(Long idMovimento) {
        logger.info("Ricerca articoli per movimento ID: {}", idMovimento);

        Movimento movimento = movimentoRepo.findById(idMovimento)
                .orElseThrow(() -> {
                    logger.warn("Movimento non trovato: ID {}", idMovimento);
                    return new IllegalArgumentException("Movimento non trovato");
                });

        List<ArticoloResponseDTO> articoli = articoloRepo.findByMovimento(movimento).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        logger.info("Trovati {} articoli per movimento ID: {}", articoli.size(), idMovimento);
        return articoli;
    }

}




