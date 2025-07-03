package com.comprooro.backend.repo;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.comprooro.backend.model.Utente;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, String> {
    
	boolean existsByEmail(String email);
	boolean existsByCodiceFiscale(String codiceFiscale);
	Optional<Utente> findByEmail(String email);
	Optional<Utente> findByCodiceFiscale(String codiceFiscale);

}


