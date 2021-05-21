package it.prova.pokeronline.repository.utente;

import java.util.List;

import it.prova.pokeronline.model.Ruolo;
import it.prova.pokeronline.model.Utente;

public interface CustomUtenteRepository {

	void aggiungiRuolo(Utente utenteEsistente, Ruolo ruoloInstance);

	List<Utente> findByExample(Utente example);

}
