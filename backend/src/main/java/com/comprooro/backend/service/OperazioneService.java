package com.comprooro.backend.service;

import java.util.List;
import java.util.Optional;

import com.comprooro.backend.dto.OperazioneRequestDTO;
import com.comprooro.backend.dto.OperazioneResponseDTO;

public interface OperazioneService {

	OperazioneResponseDTO register(OperazioneRequestDTO requestDTO);
	List<OperazioneResponseDTO> findAll();
	Optional<OperazioneResponseDTO> findById(Long id);

}
