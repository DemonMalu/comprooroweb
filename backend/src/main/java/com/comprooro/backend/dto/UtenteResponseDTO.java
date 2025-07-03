package com.comprooro.backend.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UtenteResponseDTO {

	private String username;
    private String nome;
    private String cognome;
    private LocalDate dataNascita;
    private String citta;
    private String email;
    private byte[] documentoScannerizzato;
    private String contentType;
    private String codiceFiscale;
    private String indirizzo;
    private String ruolo;

	public UtenteResponseDTO(String username, String nome, String cognome, LocalDate dataNascita, String citta,
			String email, byte[] documentoScannerizzato, String contentType, String codiceFiscale, String indirizzo,
			String ruolo) {
		this.username = username;
		this.nome = nome;
		this.cognome = cognome;
		this.dataNascita = dataNascita;
		this.citta = citta;
		this.email = email;
		this.documentoScannerizzato = documentoScannerizzato;
		this.contentType = contentType;
		this.codiceFiscale = codiceFiscale;
		this.indirizzo = indirizzo;
		this.ruolo = ruolo;
	}
	
	public UtenteResponseDTO() {
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public LocalDate getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getDocumentoScannerizzato() {
		return documentoScannerizzato;
	}

	public void setDocumentoScannerizzato(byte[] documentoScannerizzato) {
		this.documentoScannerizzato = documentoScannerizzato;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

}
