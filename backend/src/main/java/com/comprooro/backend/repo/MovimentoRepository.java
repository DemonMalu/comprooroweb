package com.comprooro.backend.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.comprooro.backend.model.Movimento;

@Repository
public interface MovimentoRepository extends JpaRepository<Movimento, Long>{

	List<Movimento> findByUtente_Username(String username);
	
	List<Movimento> findByData(LocalDate data);

}


