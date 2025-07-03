package com.comprooro.backend.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ArticoloResponseDTO {

	private Long idArticolo;
    private String nome;
    private String descrizione;
    private BigDecimal grammi;
    private String caratura;
    private byte[] foto1;
    private String contentType;
    private byte[] foto2;
    private String contentType2;
    private Long idMovimento;
    
	public ArticoloResponseDTO(Long idArticolo, String nome, String descrizione, BigDecimal grammi, String caratura,
			byte[] foto1, String contentType, byte[] foto2, String contentType2, Long idMovimento) {
		this.idArticolo = idArticolo;
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

	public ArticoloResponseDTO() {
		
	}

	public Long getIdArticolo() {
		return idArticolo;
	}

	public void setIdArticolo(Long idArticolo) {
		this.idArticolo = idArticolo;
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
