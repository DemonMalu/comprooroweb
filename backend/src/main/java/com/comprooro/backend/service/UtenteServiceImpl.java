package com.comprooro.backend.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.comprooro.backend.dto.UtenteRequestDTO;
import com.comprooro.backend.dto.UtenteResponseDTO;
import com.comprooro.backend.model.Utente;
import com.comprooro.backend.repo.UtenteRepository;

@Service
public class UtenteServiceImpl implements UtenteService {

    private static final Logger logger = LoggerFactory.getLogger(UtenteServiceImpl.class);

    private final UtenteRepository utenteRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UtenteServiceImpl(UtenteRepository utenteRepo, PasswordEncoder passwordEncoder) {
        this.utenteRepo = utenteRepo;
        this.passwordEncoder = passwordEncoder;
    }
    
    private boolean startsWithUppercase(String value) {
        return value != null && !value.isEmpty() && Character.isUpperCase(value.charAt(0));
    }

    @Override
    public String register(UtenteRequestDTO requestDTO, MultipartFile documento) {
        logger.info("Tentativo di registrazione per username: {}", requestDTO.getUsername());

        if (utenteRepo.existsById(requestDTO.getUsername())) {
            logger.warn("Registrazione fallita: Username {} già in uso.", requestDTO.getUsername());
            return "Username già in uso.";
        }
        
        if (requestDTO.getPassword().length() < 5) {
        	logger.warn("Registrazione fallita: Password {} troppo corta, inserire almeno 5 caratteri.", requestDTO.getPassword());
        	return "Password troppo corta, deve essere di almeno 5 caratteri";
        }
        
        if (!startsWithUppercase(requestDTO.getNome()) || !startsWithUppercase(requestDTO.getCognome()) ||
                !startsWithUppercase(requestDTO.getCitta()) || !startsWithUppercase(requestDTO.getIndirizzo())) {
                throw new IllegalArgumentException("Nome, Cognome, Città e Indirizzo devono iniziare con una lettera maiuscola.");
            }
        
        if(utenteRepo.existsByEmail(requestDTO.getEmail())) {
        	logger.warn("Registrazione fallita: Email {} già in uso.", requestDTO.getEmail());
        	return "Email già in uso.";
        }
        	
        if (utenteRepo.existsByCodiceFiscale(requestDTO.getCodiceFiscale())) {
            logger.warn("Registrazione fallita: Codice fiscale {} già in uso.", requestDTO.getCodiceFiscale());
            return "Codice fiscale già in uso.";
        }

        String hashedPassword = passwordEncoder.encode(requestDTO.getPassword());

        byte[] documentoBytes = null;
        String contentType = null;

        if (documento != null && !documento.isEmpty()) {
            try {
                documentoBytes = documento.getBytes();
                contentType = documento.getContentType();
            } catch (IOException e) {
                logger.error("Errore durante la lettura del documento: {}", e.getMessage());
                return "Errore durante il caricamento del documento.";
            }
        } else {
        	logger.error("Registrazione fallita: Il documento scannerizzato è obbligatorio.");
        	return "Documento scannerizzato obbligatorio";
        }

        Utente utente = new Utente(
                requestDTO.getUsername(),
                hashedPassword,
                requestDTO.getNome(),
                requestDTO.getCognome(),
                requestDTO.getDataNascita(),
                requestDTO.getCitta(),
                requestDTO.getEmail(),
                documentoBytes,
                contentType,
                requestDTO.getCodiceFiscale(),
                requestDTO.getIndirizzo(),
                "CLIENT"
        );

        utenteRepo.save(utente);
        logger.info("Registrazione completata per username: {}", requestDTO.getUsername());
        return "Registrazione completata.";
    }

    @Override
    public Optional<UtenteResponseDTO> findByUsername(String username) {
        logger.info("Ricerca utente per username: {}", username);
        Optional<UtenteResponseDTO> utenteOpt = utenteRepo.findById(username)
                .map(utente -> new UtenteResponseDTO(
                        utente.getUsername(),
                        utente.getNome(),
                        utente.getCognome(),
                        utente.getDataNascita(),
                        utente.getCitta(),
                        utente.getEmail(),
                        utente.getDocumentoScannerizzato(),
                        utente.getContentType(),
                        utente.getCodiceFiscale(),
                        utente.getIndirizzo(),
                        utente.getRuolo()
                ));
        
        if (utenteOpt.isPresent()) {
            logger.info("Utente trovato: {}", username);
        } else {
            logger.warn("Utente non trovato: {}", username);
        }

        return utenteOpt;
    }
    
    @Override
    public Optional<UtenteResponseDTO> findClienteByUsername(String username) {
        logger.info("Ricerca cliente per username: {}", username);
        Optional<UtenteResponseDTO> utenteOpt = utenteRepo.findById(username)
                .map(utente -> new UtenteResponseDTO(
                        utente.getUsername(),
                        utente.getNome(),
                        utente.getCognome(),
                        utente.getDataNascita(),
                        utente.getCitta(),
                        utente.getEmail(),
                        utente.getDocumentoScannerizzato(),
                        utente.getContentType(),
                        utente.getCodiceFiscale(),
                        utente.getIndirizzo(),
                        utente.getRuolo()
                ));
        
        if (utenteOpt.isPresent()) {
            UtenteResponseDTO utente = utenteOpt.get();

            if (!"CLIENT".equals(utente.getRuolo())) {
                logger.warn("Accesso negato: l'utente {} non ha il ruolo CLIENT", username);
                throw new RuntimeException("L'utente non ha il ruolo CLIENT");
            }

            logger.info("Cliente trovato: {}", username);
            return utenteOpt;
        } else {
            logger.warn("Cliente non trovato: {}", username);
            return Optional.empty();
        }
    }

    @Override
    public List<UtenteResponseDTO> findAllUsers() {
        logger.info("Recupero di tutti gli utenti.");
        List<UtenteResponseDTO> utenti = utenteRepo.findAll().stream()
                .map(utente -> new UtenteResponseDTO(
                        utente.getUsername(),
                        utente.getNome(),
                        utente.getCognome(),
                        utente.getDataNascita(),
                        utente.getCitta(),
                        utente.getEmail(),
                        utente.getDocumentoScannerizzato(),
                        utente.getContentType(),
                        utente.getCodiceFiscale(),
                        utente.getIndirizzo(),
                        utente.getRuolo()
                ))
                .collect(Collectors.toList());
        logger.info("Trovati {} utenti.", utenti.size());
        return utenti;
    }
    
    @Override
    public Boolean existByUsername(String username) {
        return utenteRepo.findById(username)
                .filter(utente -> utente.getRuolo().equalsIgnoreCase("CLIENT"))
                .map(utente -> {
                    logger.info("L'utente CLIENTE con username '{}' esiste.", username);
                    return true;
                })
                .orElseGet(() -> {
                    logger.info("L'utente CLIENTE con username '{}' non esiste.", username);
                    return false;
                });
    }

    @Override
    public Boolean existByCodiceFiscale(String codiceFiscale) {
        return utenteRepo.findByCodiceFiscale(codiceFiscale)
                .filter(utente -> utente.getRuolo().equalsIgnoreCase("CLIENT"))
                .map(utente -> {
                    logger.info("L'utente CLIENTE con codice fiscale '{}' esiste.", codiceFiscale);
                    return true;
                })
                .orElseGet(() -> {
                    logger.info("L'utente CLIENTE con codice fiscale '{}' non esiste.", codiceFiscale);
                    return false;
                });
    }

    @Override
    public Boolean existByEmail(String email) {
        return utenteRepo.findByEmail(email)
                .filter(utente -> utente.getRuolo().equalsIgnoreCase("CLIENT"))
                .map(utente -> {
                    logger.info("L'utente CLIENTE con email '{}' esiste.", email);
                    return true;
                })
                .orElseGet(() -> {
                    logger.info("L'utente CLIENTE con email '{}' non esiste.", email);
                    return false;
                });
    }
    
}




