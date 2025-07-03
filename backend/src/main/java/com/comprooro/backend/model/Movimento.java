package com.comprooro.backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "movimento")
public class Movimento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idMovimento;
	
	@Column(name = "data", nullable = false)
	private LocalDate data;
	
	@Column(name = "modalita_pagamento", length = 20, nullable = false)
	private String modalitaPagamento;
	
	@Column(name = "importo", precision = 20, scale = 2, nullable = false)
    private BigDecimal importo;
	
	@Lob
    @Column(name = "assegno_scannerizzato", columnDefinition = "MEDIUMBLOB", nullable = false)
    private byte[] assegnoScannerizzato;

    @Column(name = "content_type", nullable = false)
    private String contentType;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    private Utente utente;
    
    @OneToMany(mappedBy = "movimento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Articolo> articoli = new ArrayList<>();

	public Movimento(Long idMovimento, LocalDate data, String modalitaPagamento, BigDecimal importo,
			byte[] assegnoScannerizzato, String contentType, Utente utente) {
		this.idMovimento = idMovimento;
		this.data = data;
		this.modalitaPagamento = modalitaPagamento;
		this.importo = importo;
		this.assegnoScannerizzato = assegnoScannerizzato;
		this.contentType = contentType;
		setUtente(utente);
	}

	public Movimento() {
		
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

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
        if (utente != null && !utente.getMovimenti().contains(this)) {
            utente.getMovimenti().add(this);
        }
	}
	
	public List<Articolo> getArticoli() {
		return articoli;
	}

    public void setArticoli(List<Articolo> articoli) {
        this.articoli.clear();
        if (articoli != null) {
            articoli.forEach(this::addArticolo);
        }
    }
        
    public void addArticolo(Articolo articolo) {
        articoli.add(articolo);
        articolo.setMovimento(this);
    }

}


