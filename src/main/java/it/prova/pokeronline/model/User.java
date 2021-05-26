package it.prova.pokeronline.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "user")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotBlank(message = "{nome.notblank}")
	@Column(name = "nome")
	private String nome;

	@NotBlank(message = "{cognome.notblank}")
	@Column(name = "cognome")
	private String cognome;

	@NotBlank(message = "{username.notblank}")
	@Column(name = "username", unique = true)
	private String username;

	@NotBlank(message = "{password.notblank}")
	@Column(name = "password")
	private String password;

	@NotBlank(message = "{email.notblank}")
	@Column(name = "email")
	private String email;

	@NotNull(message = "{dataRegistrazione.notnull}")
	@Column(name = "data_registrazione")
	private Date dataRegistrazione;

	@Column(name = "enabled")
	@NotNull
	private Boolean enabled;

	@NotNull(message = "{esperienzaAccumulata.notnull}")
	@DecimalMin("0.0")
	@Column(name = "esperienza_accumulata")
	private Double esperienzaAccumulata;

	@NotNull(message = "{credito.notnull}")
	@DecimalMin("0.0")
	@Column(name = "credito")
	private Double credito;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_authorities", joinColumns = {
			@JoinColumn(name = "user_username", referencedColumnName = "username") }, inverseJoinColumns = {
					@JoinColumn(name = "authority_id", referencedColumnName = "id") })
	private List<Authority> authorities;

	@JsonIgnoreProperties(value = { "utenti", "utenteCreazione" })
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tavolo_id")
	private Tavolo tavolo;

	public User() {
	}

	public User(String nome, String cognome, String username, String password, Date dataRegistrazione,
			Double esperienzaAccumulata, Double credito) {
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		this.password = password;
		this.dataRegistrazione = dataRegistrazione;
		this.esperienzaAccumulata = esperienzaAccumulata;
		this.credito = credito;
	}

	public User(Long id, String nome, String cognome, String username, String password, Date dataRegistrazione,
			Boolean enabled, Double esperienzaAccumulata, Double credito, List<Authority> authorities, Tavolo tavolo) {
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		this.password = password;
		this.dataRegistrazione = dataRegistrazione;
		this.enabled = enabled;
		this.esperienzaAccumulata = esperienzaAccumulata;
		this.credito = credito;
		this.authorities = authorities;
		this.tavolo = tavolo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getDataRegistrazione() {
		return dataRegistrazione;
	}

	public void setDataRegistrazione(Date dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	public Double getEsperienzaAccumulata() {
		return esperienzaAccumulata;
	}

	public void setEsperienzaAccumulata(Double esperienzaAccumulata) {
		this.esperienzaAccumulata = esperienzaAccumulata;
	}

	public Double getCredito() {
		return credito;
	}

	public void setCredito(Double credito) {
		this.credito = credito;
	}

	public Tavolo getTavolo() {
		return tavolo;
	}

	public void setTavolo(Tavolo tavolo) {
		this.tavolo = tavolo;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "User: [id=" + id + ", username=" + username + ", esperienzaAccumulata=" + esperienzaAccumulata
				+ ", credito=" + credito + "]";
	}

}
