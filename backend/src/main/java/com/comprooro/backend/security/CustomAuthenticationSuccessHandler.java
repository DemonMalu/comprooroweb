package com.comprooro.backend.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        String username = authentication.getName();

        logger.info("Utente '{}' autenticato con successo. Ruoli: {}", username, roles);

        if (roles.contains("ROLE_ADMIN")) {
            logger.info("Reindirizzamento dell'utente '{}' all'area amministratore.", username);
            response.sendRedirect("http://localhost:5173/api/home/amministratore");
        } else if (roles.contains("ROLE_OPERATOR")) {
            logger.info("Reindirizzamento dell'utente '{}' all'area operatore.", username);
            response.sendRedirect("http://localhost:5173/api/home/operatore");
        } else if (roles.contains("ROLE_CLIENT")) {
            logger.info("Reindirizzamento dell'utente '{}' all'area cliente.", username);
            response.sendRedirect("http://localhost:5173/api/home/cliente");
        } else {
            logger.warn("Utente '{}' senza ruolo valido. Accesso negato.", username);
            response.sendRedirect("http://localhost:8080/login");
        }
    }

}




