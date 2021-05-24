package it.prova.pokeronline.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.prova.pokeronline.model.Ruolo;
import it.prova.pokeronline.repository.ruolo.RuoloRepository;

@Service
public class RuoloServiceImpl implements RuoloService {

	@Autowired
	private RuoloRepository repository;

	@Override
	public List<Ruolo> listAllElements() {
		return (List<Ruolo>) repository.findAll();
	}

	@Override
	public Ruolo caricaSingoloElemento(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public Ruolo aggiorna(Ruolo ruoloInstance) {
		return repository.save(ruoloInstance);
	}

	@Override
	public Ruolo inserisciNuovo(Ruolo ruoloInstance) {
		return repository.save(ruoloInstance);
	}

	@Override
	public void rimuovi(Ruolo ruoloInstance) {
		repository.delete(ruoloInstance);
	}

	@Override
	public Ruolo findById(Long id) {
		return repository.findById(id).orElse(null);
	}

}
