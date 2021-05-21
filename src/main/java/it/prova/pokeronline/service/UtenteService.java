package it.prova.pokeronline.service;

import java.util.List;

import it.prova.pokeronline.model.Utente;

public interface UtenteService {

	List<Utente> listAllElements();

	Utente caricaSingoloElemento(Long id);

	Utente aggiorna(Utente utenteInstance);

	Utente inserisciNuovo(Utente utenteInstance);

	void rimuovi(Utente utenteInstance);

	List<Utente> findByExample(Utente example);
}
