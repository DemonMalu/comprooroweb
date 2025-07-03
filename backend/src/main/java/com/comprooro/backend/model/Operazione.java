package com.comprooro.backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "operazione")
public class Operazione {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idOperazione;
	
	@Column(name = "descrizione", length = 30, nullable = false)
	private String descrizione;
	
	@Column(name = "tipo", nullable = false, columnDefinition = "TINYINT")
	private int tipo;
	
	@Column(name = "importo", precision = 20, scale = 2, nullable = false)
    private BigDecimal importo;
	
	@Column(name = "data", nullable = false)
	private LocalDate data;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    private Utente utente;

	public Operazione(Long idOperazione, String descrizione, int tipo, BigDecimal importo, LocalDate data,
			Utente utente) {
		this.idOperazione = idOperazione;
		this.descrizione = descrizione;
		this.tipo = tipo;
		this.importo = importo;
		this.data = data;
		this.utente = utente;
	}
	
	public Operazione() {
		
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

	public Utente getUtente() {
		return utente;
	}
	
	public void setUtente(Utente utente) {
		this.utente = utente;
        if (utente != null && !utente.getOperazioni().contains(this)) {
            utente.getOperazioni().add(this);
        }
	}

}
