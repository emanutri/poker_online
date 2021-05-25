package it.prova.pokeronline.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.prova.pokeronline.model.Authority;
import it.prova.pokeronline.model.User;
import it.prova.pokeronline.security.repository.UserRepository;

@Service
public class UtenteServiceImpl implements UtenteService {

	@Autowired
	private UserRepository repository;

	@Override
	public List<User> listAllElements() {
		return (List<User>) repository.findAll();
	}

	@Override
	public User caricaSingoloElemento(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public User aggiorna(User utenteInstance) {
		return repository.save(utenteInstance);
	}

	@Override
	public User inserisciNuovo(User utenteInstance) {
		return repository.save(utenteInstance);
	}

	@Override
	public void rimuovi(User utenteInstance) {
		repository.delete(utenteInstance);
	}

	@Override
	public List<User> findByExample(User example) {
		return repository.findByExample(example);
	}

	@Override
	public void aggiungiRuolo(User utenteEsistente, Authority ruoloInstance) {
		repository.aggiungiRuolo(utenteEsistente, ruoloInstance);
	}

	@Override
	public User trovaByUsername(String username) {
		return repository.findByUsername(username).orElse(null);
	}
}
