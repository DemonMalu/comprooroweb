package com.comprooro.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperazioneRequestDTO {

	@NotNull(message = "La descrizione è obbligatoria.")
	@Size(max = 30)
	private String descrizione;
	
	@NotNull(message = "Il tipo è obbligatorio.")
	private int tipo;
	
	@NotNull(message = "L'importo è obbligatorio.")
    private BigDecimal importo;
	
	@NotNull(message = "La data è obbligatoria (formato: yyyy-MM-dd).")
	private LocalDate data;
	
	@NotNull(message = "L'username è obbligatorio.")
	@Size(min = 3, max = 50)
    private String username;

	public OperazioneRequestDTO() {
		
	}

	public OperazioneRequestDTO(@NotNull(message = "La descrizione è obbligatoria.") @Size(max = 30) String descrizione,
			@NotNull(message = "Il tipo è obbligatorio.") int tipo,
			@NotNull(message = "L'importo è obbligatorio.") BigDecimal importo,
			@NotNull(message = "La data è obbligatoria.") LocalDate data,
			@NotNull(message = "L'username è obbligatorio.") @Size(min = 3, max = 50) String username) {
		this.descrizione = descrizione;
		this.tipo = tipo;
		this.importo = importo;
		this.data = data;
		this.username = username;
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
