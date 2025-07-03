package com.comprooro.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.comprooro.backend.dto.MovimentoRequestDTO;
import com.comprooro.backend.dto.MovimentoResponseDTO;

public interface MovimentoService {
	
	MovimentoResponseDTO register(MovimentoRequestDTO requestDTO, MultipartFile assegno);
	List<MovimentoResponseDTO> findAll();
	Optional<MovimentoResponseDTO> findById(Long id);
	List<MovimentoResponseDTO> findByUsername(String username);
	
}


