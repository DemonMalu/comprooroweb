package com.comprooro.backend.config;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.comprooro.backend.security.CustomAuthenticationSuccessHandler;
import com.comprooro.backend.security.CustomUserDetailsService;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomAuthenticationSuccessHandler successHandler, CustomUserDetailsService userDetailsService) {
        this.successHandler = successHandler;
        this.userDetailsService = userDetailsService;
        logger.info("SecurityConfig inizializzata.");
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
    	CorsConfiguration configuration = new CorsConfiguration();
    	configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
    	configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    	configuration.setAllowCredentials(true);
    	configuration.addAllowedHeader("*");
    	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    	source.registerCorsConfiguration("/**", configuration);
    	return source;
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configurazione della SecurityFilterChain avviata...");

        http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> {
            csrf.disable();
            logger.info("Disabilitando la protezione CSRF...");
        })
        .authorizeHttpRequests(requests -> {
            requests
                .requestMatchers("/api/home/amministratore/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/api/home/operatore/**").hasAuthority("ROLE_OPERATOR")
                .requestMatchers("/api/home/cliente/**").hasAuthority("ROLE_CLIENT")
                .requestMatchers("/api/report/**").hasAnyAuthority("ROLE_OPERATOR", "ROLE_ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_CLIENT")
                .requestMatchers(HttpMethod.POST, "/api/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_OPERATOR")
                .anyRequest().authenticated();
            logger.info("Regole di autorizzazione configurate.");
        })
        .formLogin(form -> {
            form.successHandler(successHandler)
                .permitAll();
            logger.info("Configurazione del login completata.");
        })
        .sessionManagement(session -> {
            session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
                .expiredUrl("/login?expired");
            logger.info("Gestione delle sessioni configurata: max 1 sessione per utente.");
        })
        .httpBasic(Customizer.withDefaults());

        logger.info("SecurityFilterChain creata con successo.");
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("PasswordEncoder (BCrypt) creato.");
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
    	DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    	provider.setUserDetailsService(userDetailsService);
    	provider.setPasswordEncoder(passwordEncoder());
    	return provider;
    }
    
}




