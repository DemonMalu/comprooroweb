package com.comprooro.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperazioneResponseDTO {

	private Long idOperazione;
	private String descrizione;
	private int tipo;
	private BigDecimal importo;
	private LocalDate data;
	private String username;
	
	public OperazioneResponseDTO() {
		
	}

	public OperazioneResponseDTO(Long idOperazione, String descrizione, int tipo, BigDecimal importo, LocalDate data, String username) {
		this.idOperazione = idOperazione;
		this.descrizione = descrizione;
		this.tipo = tipo;
		this.importo = importo;
		this.data = data;
		this.username = username;
	}
	
	public Long getIdOperazione() {
		return idOperazione;
	}

	public void setIdOperazione(Long idOperazione) {
		this.idOperazione = idOperazione;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getImporto() {
		return importo;
	}

	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
