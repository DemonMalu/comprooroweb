package com.comprooro.backend.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "utente")
public class Utente {

	@Id
	@Column(name = "username", length = 50, nullable = false)
	private String username;

	@Column(name = "password_hash", nullable = false, columnDefinition = "CHAR(60)")
    private String password;

	@Column(name = "nome", length = 50)
	private String nome;

	@Column(name = "cognome", length = 50)
	private String cognome;

	@Column(name = "data_nascita")
	private LocalDate dataNascita;

	@Column(name = "citta", length = 100)
	private String citta;

	@Column(name = "email", length = 255)
	private String email;

	@Lob
    @Column(name = "documento_scannerizzato", columnDefinition = "MEDIUMBLOB")
    private byte[] documentoScannerizzato;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "codice_fiscale", columnDefinition = "CHAR(16)", unique = true)
    private String codiceFiscale;

    @Column(name = "indirizzo", length = 255)
    private String indirizzo;

    @Column(name = "ruolo", length = 13, nullable = false)
    private String ruolo;
    
    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Movimento> movimenti = new ArrayList<>();
    
    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Operazione> operazioni = new ArrayList<>();

	public Utente(String username, String password, String nome, String cognome, LocalDate dataNascita,
			String citta, String email, byte[] documentoScannerizzato, String contentType, String codiceFiscale,
			String indirizzo, String ruolo) {
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

	public Utente() {

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
	
	public List<Movimento> getMovimenti() {
		return movimenti;
	}

    public void setMovimenti(List<Movimento> movimenti) {
        this.movimenti.clear();
        if (movimenti != null) {
            movimenti.forEach(this::addMovimento);
        }
    }
        
    public void addMovimento(Movimento movimento) {
        movimenti.add(movimento);
        movimento.setUtente(this);
    }
    
    public List<Operazione> getOperazioni() {
		return operazioni;
	}

    public void setOperazioni(List<Operazione> operazioni) {
        this.operazioni.clear();
        if (operazioni != null) {
            operazioni.forEach(this::addOperazione);
        }
    }
        
    public void addOperazione(Operazione operazione) {
        operazioni.add(operazione);
        operazione.setUtente(this);
    }

}




