package it.prova.pokeronline.service;

import java.util.List;

import it.prova.pokeronline.model.Authority;
import it.prova.pokeronline.model.User;

public interface UserService {

	List<User> listAllElements();

	User caricaSingoloElemento(Long id);

	User aggiorna(User utenteInstance);

	User inserisciNuovo(User utenteInstance);

	void rimuovi(User utenteInstance);

	List<User> findByExample(User example);

	void aggiungiRuolo(User utenteEsistente, Authority ruoloInstance);

	User trovaByUsername(String username);
}
