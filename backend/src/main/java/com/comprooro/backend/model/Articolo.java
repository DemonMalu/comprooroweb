package com.comprooro.backend.model;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "articolo")
public class Articolo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idArticolo;
	
	@Column(name = "nome", nullable = false, length = 50)
	private String nome;
	
	@Column(name = "descrizione", length = 255)
	private String descrizione;
	
	@Column(name = "grammi", nullable = false, precision = 10, scale = 2)
	private BigDecimal grammi;
	
	@Column(name = "caratura", nullable = false, length = 7)
	private String caratura;
	
	@Lob
	@Column(name = "foto_1", columnDefinition = "MEDIUMBLOB")
	private byte[] foto1;
	
	@Column(name = "content_type")
    private String contentType;
	
	@Lob
	@Column(name = "foto_2", columnDefinition = "MEDIUMBLOB")
	private byte[] foto2;
	
	@Column(name = "content_type_2")
    private String contentType2;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_movimento", nullable = false)
	private Movimento movimento;

	public Articolo(Long idArticolo, String nome, String descrizione, BigDecimal grammi, String caratura,
			byte[] foto1, String contentType, byte[] foto2, String contentType2, Movimento movimento) {
		this.idArticolo = idArticolo;
		this.nome = nome;
		this.descrizione = descrizione;
		this.grammi = grammi;
		this.caratura = caratura;
		this.foto1 = foto1;
		this.contentType = contentType;
		this.foto2 = foto2;
		this.contentType2 = contentType2;
		this.movimento = movimento;
	}

	public Articolo() {
		
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

	public Movimento getMovimento() {
		return movimento;
	}

	public void setMovimento(Movimento movimento) {
		this.movimento = movimento;
        if (movimento != null && !movimento.getArticoli().contains(this)) {
            movimento.getArticoli().add(this);
        }
	}

}
