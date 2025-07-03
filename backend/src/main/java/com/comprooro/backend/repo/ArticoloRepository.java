package com.comprooro.backend.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.comprooro.backend.model.Articolo;
import com.comprooro.backend.model.Movimento;

@Repository
public interface ArticoloRepository extends JpaRepository<Articolo, Long> {

	List<Articolo> findByMovimento(Movimento movimento);

}
