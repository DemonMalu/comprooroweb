package com.comprooro.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovimentoResponseDTO {

	private Long idMovimento;
	private LocalDate data;
    private String modalitaPagamento;
    private BigDecimal importo;
    private String username;
    private byte[] assegnoScannerizzato;
    private String contentType;
    private List<ArticoloResponseDTO> articoli;
    
    public MovimentoResponseDTO(Long idMovimento, LocalDate data, String modalitaPagamento, BigDecimal importo,
			String username, byte[] assegnoScannerizzato, String contentType, List<ArticoloResponseDTO> articoli) {
		this.idMovimento = idMovimento;
		this.data = data;
		this.modalitaPagamento = modalitaPagamento;
		this.importo = importo;
		this.username = username;
		this.assegnoScannerizzato = assegnoScannerizzato;
		this.contentType = contentType;
		this.articoli = articoli;
	}

	public MovimentoResponseDTO() {
    	
    }

    public Long getIdMovimento() {
    	return idMovimento;
    	}
    public void setIdMovimento(Long idMovimento) {
    	this.idMovimento = idMovimento;
    	}

    public LocalDate getData() {
    	return data;
    	}
    public void setData(LocalDate data) {
    	this.data = data;
    	}

    public String getModalitaPagamento() {
    	return modalitaPagamento;
    	}
    public void setModalitaPagamento(String modalitaPagamento) {
    	this.modalitaPagamento = modalitaPagamento;
    	}

    public BigDecimal getImporto() {
    	return importo;
    }
    public void setImporto(BigDecimal importo) {
    	this.importo = importo;
    	}

    public String getUsername() {
    	return username;
    	}
    public void setUsername(String username) {
    	this.username = username;
    	}

    public byte[] getAssegnoScannerizzato() {
    	return assegnoScannerizzato;
    	}
    public void setAssegnoScannerizzato(byte[] assegnoScannerizzato) {
        this.assegnoScannerizzato = assegnoScannerizzato;
    }
    
    public String getContentType() {
    	return contentType;
    	}
    public void setContentType(String contentType) {
    	this.contentType = contentType;
    	}

	public List<ArticoloResponseDTO> getArticoli() {
		return articoli;
	}

	public void setArticoli(List<ArticoloResponseDTO> articoli) {
		this.articoli = articoli;
	}

}
