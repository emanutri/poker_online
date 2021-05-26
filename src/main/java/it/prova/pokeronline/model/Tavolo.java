package it.prova.pokeronline.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "tavolo")
public class Tavolo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotNull(message = "{esperienzaMin.notnull}")
	@DecimalMin("0.0")
	@Column(name = "esperienza_min")
	private Double esperienzaMin;

	@NotNull(message = "{cifraMinima.notnull}")
	@DecimalMin("0.0")
	@Column(name = "cifra_minima")
	private Double cifraMinima;

	@NotBlank(message = "{denominazione.notblank}")
	@Column(name = "denominazione")
	private String denominazione;

	@NotNull(message = "{dataCreazione.notnull}")
	@Column(name = "data_creazione")
	private Date dataCreazione;

	@JsonIgnoreProperties(value = { "tavolo" })
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tavolo")
	private Set<User> utenti = new HashSet<>(0);

	@JsonIgnoreProperties(value = { "tavolo" })
	@NotNull(message = "{utenteCreazione.notnull}")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "utente_creazione")
	private User utenteCreazione;

	public Tavolo() {
	}

	public Tavolo(Double esperienzaMin, Double cifraMinima, String denominazione, Date dataCreazione,
			User utenteCreazione) {
		this.esperienzaMin = esperienzaMin;
		this.cifraMinima = cifraMinima;
		this.denominazione = denominazione;
		this.dataCreazione = dataCreazione;
		this.utenteCreazione = utenteCreazione;
	}

	public Tavolo(Long id, Double esperienzaMin, Double cifraMinima, String denominazione, Date dataCreazione,
			Set<User> utenti, User utenteCreazione) {
		this.id = id;
		this.esperienzaMin = esperienzaMin;
		this.cifraMinima = cifraMinima;
		this.denominazione = denominazione;
		this.dataCreazione = dataCreazione;
		this.utenti = utenti;
		this.utenteCreazione = utenteCreazione;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getEsperienzaMin() {
		return esperienzaMin;
	}

	public void setEsperienzaMin(Double esperienzaMin) {
		this.esperienzaMin = esperienzaMin;
	}

	public Double getCifraMinima() {
		return cifraMinima;
	}

	public void setCifraMinima(Double cifraMinima) {
		this.cifraMinima = cifraMinima;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Set<User> getUtenti() {
		return utenti;
	}

	public void setUtenti(Set<User> utenti) {
		this.utenti = utenti;
	}

	public User getUtenteCreazione() {
		return utenteCreazione;
	}

	public void setUtenteCreazione(User utenteCreazione) {
		this.utenteCreazione = utenteCreazione;
	}

	@Override
	public String toString() {
		return "Tavolo [id=" + id + ", esperienzaMin=" + esperienzaMin + ", cifraMinima=" + cifraMinima
				+ ", denominazione=" + denominazione + ", dataCreazione=" + dataCreazione + "]";
	}

}
