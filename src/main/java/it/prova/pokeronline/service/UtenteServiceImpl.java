package it.prova.pokeronline.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.prova.pokeronline.model.Ruolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.repository.utente.UtenteRepository;

@Service
public class UtenteServiceImpl implements UtenteService {

	@Autowired
	private UtenteRepository repository;

	@Override
	public List<Utente> listAllElements() {
		return (List<Utente>) repository.findAll();
	}

	@Override
	public Utente caricaSingoloElemento(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public Utente aggiorna(Utente utenteInstance) {
		return repository.save(utenteInstance);
	}

	@Override
	public Utente inserisciNuovo(Utente utenteInstance) {
		return repository.save(utenteInstance);
	}

	@Override
	public void rimuovi(Utente utenteInstance) {
		repository.delete(utenteInstance);
	}

	@Override
	public List<Utente> findByExample(Utente example) {
		return repository.findByExample(example);
	}

	@Override
	public void aggiungiRuolo(Utente utenteEsistente, Ruolo ruoloInstance) {
		// ruoloRepository.findById(ruoloInstance.getId());
		repository.aggiungiRuolo(utenteEsistente, ruoloInstance);
	}

}
