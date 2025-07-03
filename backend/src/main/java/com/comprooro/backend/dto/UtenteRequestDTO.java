package com.comprooro.backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UtenteRequestDTO {

	@NotBlank(message = "Lo username è obbligatorio.")
    @Size(min = 3, max = 50, message = "Lo username deve essere tra 3 e 50 caratteri.")
    private String username;

    @NotBlank(message = "La password è obbligatoria.")
    @Size(min = 5, message = "La password deve avere almeno 5 caratteri.")
    private String password;
    
    @Size(max = 50)
    private String nome;

    @Size(max = 50)
    private String cognome;

    private LocalDate dataNascita;

    @Size(max = 100)
    private String citta;

    @Email @Size(max = 255)
    private String email;
    
    private byte[] documentoScannerizzato;
    
    private String contentType;

    @Size(max = 16, min = 16) @Pattern(regexp = "[A-Z0-9]{16}")
    private String codiceFiscale;

    @Size(max = 255)
    private String indirizzo;

    @NotBlank @Size(max = 13)
    private String ruolo;

	public UtenteRequestDTO(
			@NotBlank(message = "Lo username è obbligatorio.") @Size(min = 3, max = 50, message = "Lo username deve essere tra 3 e 50 caratteri.") String username,
			@NotBlank(message = "La password è obbligatoria.") @Size(min = 5, message = "La password deve avere almeno 5 caratteri.") String password,
			@Size(max = 50) String nome, @Size(max = 50) String cognome, LocalDate dataNascita,
			@Size(max = 100) String citta, @Email @Size(max = 255) String email, byte[] documentoScannerizzato,
			String contentType, @Size(max = 16, min = 16) @Pattern(regexp = "[A-Z0-9]{16}") String codiceFiscale,
			@Size(max = 255) String indirizzo, @NotBlank @Size(max = 13) String ruolo) {
		this.username = username;
		this.password = password;
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
	
	public UtenteRequestDTO() {
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
