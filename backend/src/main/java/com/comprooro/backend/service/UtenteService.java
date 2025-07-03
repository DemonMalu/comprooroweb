package com.comprooro.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.comprooro.backend.dto.UtenteRequestDTO;
import com.comprooro.backend.dto.UtenteResponseDTO;

public interface UtenteService {

    String register(UtenteRequestDTO requestDTO, MultipartFile documento);
    Optional<UtenteResponseDTO> findByUsername(String username);
    Optional<UtenteResponseDTO> findClienteByUsername(String username);
    List<UtenteResponseDTO> findAllUsers();
    Boolean existByUsername(String username);
    Boolean existByCodiceFiscale(String codiceFiscale);
    Boolean existByEmail(String email);

}


