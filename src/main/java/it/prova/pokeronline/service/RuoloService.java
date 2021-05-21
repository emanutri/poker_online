package it.prova.pokeronline.service;

import java.util.List;

import it.prova.pokeronline.model.Ruolo;

public interface RuoloService {

	List<Ruolo> listAllElements();

	Ruolo caricaSingoloElemento(Long id);

	Ruolo aggiorna(Ruolo ruoloInstance);

	Ruolo inserisciNuovo(Ruolo ruoloInstance);

	void rimuovi(Ruolo ruoloInstance);

}
