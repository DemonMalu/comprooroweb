package com.comprooro.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MovimentoRequestDTO {

	@NotNull(message = "La data è obbligatoria (formato: dd-MM-yyyy).")
	private LocalDate data;
	
	@NotNull(message = "La modalità di pagamento è obbligatoria.")
	@Size(max = 20)
	private String modalitaPagamento;
	
	@NotNull(message = "L'importo è obbligatorio.")
    private BigDecimal importo;

	@NotNull(message = "L'assegno scannerizzato è obbligatorio.")
    private byte[] assegnoScannerizzato;
    
	@NotNull(message = "Il content type è obbligatorio.")
    private String contentType;

    @NotNull(message = "L'username è obbligatorio.")
    @Size(min = 3, max = 50)
    private String username;
    
    List<ArticoloRequestDTO> articoli;

	public MovimentoRequestDTO(@NotNull(message = "La data è obbligatoria (formato: dd-MM-yyyy).") LocalDate data,
			@NotNull(message = "La modalità di pagamento è obbligatoria.") @Size(max = 20) String modalitaPagamento,
			@NotNull(message = "L'importo è obbligatorio.") BigDecimal importo,
			@NotNull(message = "L'assegno scannerizzato è obbligatorio.") byte[] assegnoScannerizzato,
			@NotNull(message = "Il content type è obbligatorio.") String contentType,
			@NotNull(message = "L'username è obbligatorio.") @Size(min = 3, max = 50) String username,
			List<ArticoloRequestDTO> articoli) {
		this.data = data;
		this.modalitaPagamento = modalitaPagamento;
		this.importo = importo;
		this.assegnoScannerizzato = assegnoScannerizzato;
		this.contentType = contentType;
		this.username = username;
		this.articoli = articoli;
	}
	
	public MovimentoRequestDTO() {
		
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<ArticoloRequestDTO> getArticoli() {
		return articoli;
	}

	public void setArticoli(List<ArticoloRequestDTO> articoli) {
		this.articoli = articoli;
	}

}
