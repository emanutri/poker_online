package it.prova.pokeronline.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.repository.tavolo.TavoloRepository;

@Service
public class TavoloServiceImpl implements TavoloService {

	@Autowired
	private TavoloRepository repository;

	@Override
	public List<Tavolo> listAllElements() {
		return (List<Tavolo>) repository.findAll();
	}

	@Override
	public Tavolo caricaSingoloElemento(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public Tavolo aggiorna(Tavolo tavoloInstance) {
		return repository.save(tavoloInstance);
	}

	@Override
	public Tavolo inserisciNuovo(Tavolo tavoloInstance) {
		return repository.save(tavoloInstance);
	}

	@Override
	public void rimuovi(Tavolo tavoloInstance) {
		repository.delete(tavoloInstance);
	}

	@Override
	public List<Tavolo> findByExample(Tavolo example) {
		return repository.findByExample(example);
	}

//	@Override
//	public void accediTavolo(Tavolo tavoloInstance)
}
