package com.comprooro.backend.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ArticoloRequestDTO {

	@NotNull(message = "Il nome è obbligatorio.")
	@Size(max = 50)
	private String nome;
	
	@Size(max = 255)
    private String descrizione;
    
    @NotNull(message = "I grammi sono obbligatori.")
    private BigDecimal grammi;
    
    @NotNull(message = "La caratura è obbligatoria.")
    private String caratura;
    
    private byte[] foto1;
    
    private String contentType;
    
    private byte[] foto2;
    
    private String contentType2;
    
    @NotNull(message = "L'id del movimento è obbligatorio.")
    private Long idMovimento;

	public ArticoloRequestDTO(@NotNull(message = "Il nome è obbligatorio.") @Size(max = 50) String nome,
			@Size(max = 255) String descrizione,
			@NotNull(message = "I grammi sono obbligatori.") BigDecimal grammi,
			@NotNull(message = "La caratura è obbligatoria.") String caratura, byte[] foto1, String contentType,
			byte[] foto2, String contentType2,
			@NotNull(message = "L'id del movimento è obbligatorio.") Long idMovimento) {
		this.nome = nome;
		this.descrizione = descrizione;
		this.grammi = grammi;
		this.caratura = caratura;
		this.foto1 = foto1;
		this.contentType = contentType;
		this.foto2 = foto2;
		this.contentType2 = contentType2;
		this.idMovimento = idMovimento;
	}

	public ArticoloRequestDTO() {
		
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public BigDecimal getGrammi() {
		return grammi;
	}

	public void setGrammi(BigDecimal grammi) {
		this.grammi = grammi;
	}

	public String getCaratura() {
		return caratura;
	}

	public void setCaratura(String caratura) {
		this.caratura = caratura;
	}

	public byte[] getFoto1() {
		return foto1;
	}

	public void setFoto1(byte[] foto1) {
		this.foto1 = foto1;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public byte[] getFoto2() {
		return foto2;
	}

	public void setFoto2(byte[] foto2) {
		this.foto2 = foto2;
	}

	public String getContentType2() {
		return contentType2;
	}

	public void setContentType2(String contentType2) {
		this.contentType2 = contentType2;
	}

	public Long getIdMovimento() {
		return idMovimento;
	}

	public void setIdMovimento(Long idMovimento) {
		this.idMovimento = idMovimento;
	}

}
