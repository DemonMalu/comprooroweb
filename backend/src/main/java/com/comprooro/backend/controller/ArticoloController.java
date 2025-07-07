package com.comprooro.backend.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.comprooro.backend.dto.ArticoloRequestDTO;
import com.comprooro.backend.dto.ArticoloResponseDTO;
import com.comprooro.backend.service.ArticoloService;
import com.comprooro.backend.service.ReportService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ArticoloController {

    private static final Logger logger = LoggerFactory.getLogger(ArticoloController.class);
    private final ArticoloService articoloService;
    private final ReportService reportService;

    @Autowired
    public ArticoloController(ArticoloService articoloService, ReportService reportService) {
        this.articoloService = articoloService;
        this.reportService = reportService;
    }

    @PostMapping("/home/operatore/register-articolo")
    public ResponseEntity<byte[]> register(
    		@RequestPart("articolo") @Valid ArticoloRequestDTO requestDTO,
            @RequestPart(value = "foto1", required = false) MultipartFile foto1,
            @RequestPart(value = "foto2", required = false) MultipartFile foto2) {
    	
        logger.info("Registrazione di un nuovo articolo con descrizione: {}", requestDTO.getDescrizione());

        ArticoloResponseDTO savedArticolo = articoloService.register(requestDTO, foto1, foto2);
        logger.info("Articolo registrato con ID: {}", savedArticolo.getIdArticolo());
        logger.info("File foto1: {}", (foto1 != null) ? foto1.getOriginalFilename() : "Nessun file");
        logger.info("File foto2: {}", (foto2 != null) ? foto2.getOriginalFilename() : "Nessun file");


        byte[] pdfBytes = reportService.generaCartellinoArticoloPDF(savedArticolo);
        logger.info("Cartellino PDF generato per l'articolo ID: {}", savedArticolo.getIdArticolo());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cartellino_articolo_" + savedArticolo.getIdArticolo() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/report/all-articoli")
    public ResponseEntity<List<ArticoloResponseDTO>> findAll() {
        logger.info("Recupero di tutti gli articoli.");
        List<ArticoloResponseDTO> articoli = articoloService.findAll();
        logger.info("Recuperati {} articoli.", articoli.size());
        return ResponseEntity.ok(articoli);
    }

    @GetMapping("/report/articolo/{id}")
    public ResponseEntity<ArticoloResponseDTO> findById(@PathVariable Long id) {
        logger.info("Ricerca articolo con ID: {}", id);
        ArticoloResponseDTO articolo = articoloService.findById(id);
        if (articolo != null) {
            logger.info("Articolo trovato: {}", articolo.getDescrizione());
            return ResponseEntity.ok(articolo);
        } else {
            logger.warn("Articolo non trovato con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
    
//    @GetMapping("/report/articoli/{idMovimento}")
//    public ResponseEntity<List<ArticoloResponseDTO>> getArticoliByMovimento(@PathVariable Long idMovimento) {
//        try {
//            List<ArticoloResponseDTO> articoli = articoloService.findByMovimento(idMovimento);
//
//            if (articoli.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(null);
//            }
//
//            return ResponseEntity.ok(articoli);
//        } catch (Exception e) {
//            logger.error("Errore nel recupero degli articoli per movimento con ID: {}", idMovimento, e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

}




