package com.comprooro.backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.comprooro.backend.dto.ArticoloResponseDTO;
import com.comprooro.backend.dto.BilancioResponseDTO;
import com.comprooro.backend.dto.MovimentoResponseDTO;
import com.comprooro.backend.service.ArticoloService;
import com.comprooro.backend.service.MovimentoService;
import com.comprooro.backend.service.ReportService;

@RestController
@RequestMapping("/api")
public class ReportController {

	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
	
	private final ReportService reportService;
	private final MovimentoService movimentoService;
	private final ArticoloService articoloService;
	
	public ReportController(ReportService reportService, MovimentoService movimentoService,
			ArticoloService articoloService) {
		this.reportService = reportService;
		this.movimentoService = movimentoService;
		this.articoloService = articoloService;
	}

	@PostMapping("/report/dichiarazione/{idMovimento}")
    public ResponseEntity<byte[]> generateDichiarazioneVendita(@PathVariable Long idMovimento) {
        MovimentoResponseDTO movimento = movimentoService.findById(idMovimento)
                .orElseThrow(() -> new RuntimeException("Movimento non trovato con ID: " + idMovimento));

        List<ArticoloResponseDTO> articoli = articoloService.findByMovimento(idMovimento);

        if (articoli.isEmpty()) {
            throw new RuntimeException("Nessun articolo associato al movimento con ID: " + idMovimento);
        }

        byte[] pdfBytes = reportService.generaDichiarazioneVenditaPDF(movimento, articoli);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=dichiarazione_vendita_" + idMovimento + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
    
    @GetMapping("/home/amministratore/bilancio/{data}")
    public ResponseEntity<BilancioResponseDTO> getBilancio(@PathVariable String data) {
        try {
            LocalDate localDate = LocalDate.parse(data);
            BilancioResponseDTO bilancio = reportService.calcolaBilancio(localDate);
            return ResponseEntity.ok(bilancio);
        } catch (Exception e) {
            logger.error("Errore nel calcolo del bilancio per la data: {}", data, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
