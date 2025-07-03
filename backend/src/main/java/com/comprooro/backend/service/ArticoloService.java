package com.comprooro.backend.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.comprooro.backend.dto.ArticoloRequestDTO;
import com.comprooro.backend.dto.ArticoloResponseDTO;

public interface ArticoloService {

	ArticoloResponseDTO register(ArticoloRequestDTO requestDTO, MultipartFile foto1, MultipartFile foto2);
	List<ArticoloResponseDTO> findAll();
	ArticoloResponseDTO findById(Long id);
	List<ArticoloResponseDTO> findByMovimento(Long idMovimento);

}
