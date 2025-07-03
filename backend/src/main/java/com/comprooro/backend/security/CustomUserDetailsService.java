package com.comprooro.backend.security;

import com.comprooro.backend.model.Utente;
import com.comprooro.backend.repo.UtenteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UtenteRepository utenteRepository;

    @Autowired
    public CustomUserDetailsService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Tentativo di caricamento dell'utente: {}", username);

        Utente utente = utenteRepository.findById(username)
                .orElseThrow(() -> {
                    logger.warn("Utente non trovato: {}", username);
                    return new UsernameNotFoundException("Utente non trovato: " + username);
                });

        logger.info("Utente trovato: {} con ruolo: ROLE_{}", utente.getUsername(), utente.getRuolo());

        return new User(
                utente.getUsername(),
                utente.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + utente.getRuolo()))
        );
    }
}




