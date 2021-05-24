package it.prova.pokeronline.service;

import java.util.List;

import it.prova.pokeronline.model.Tavolo;

public interface TavoloService {

	List<Tavolo> listAllElements();

	Tavolo caricaSingoloElemento(Long id);

	Tavolo aggiorna(Tavolo tavoloInstance);

	Tavolo inserisciNuovo(Tavolo tavoloInstance);

	void rimuovi(Tavolo tavoloInstance);

	List<Tavolo> findByExample(Tavolo example);

	List<Tavolo> trovaTavoliByEsperienza(Double esperienza);
}
