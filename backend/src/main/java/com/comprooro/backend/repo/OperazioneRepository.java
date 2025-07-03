package com.comprooro.backend.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.comprooro.backend.model.Operazione;

@Repository
public interface OperazioneRepository extends JpaRepository<Operazione, Long> {
	
	List<Operazione> findByDataAndTipo(LocalDate data, int tipo);

}
