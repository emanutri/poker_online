package it.prova.pokeronline.web.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.service.RuoloService;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;
import it.prova.pokeronline.web.api.exception.UnouthorizedException;

@RestController
@RequestMapping("api/tavolo")
public class TavoloController {

	@Autowired
	private TavoloService tavoloService;

	@Autowired
	private UtenteService utenteService;

	@Autowired
	private RuoloService ruoloService;

	@GetMapping
	public List<Tavolo> getAll(@RequestHeader("authorization") String username) {

		Utente utenteInstance = utenteService.trovaByUsername(username);

		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L)) && !utenteInstance.getRuoli().contains(ruoloService.findById(3L))) {
			throw new UnouthorizedException("Utente non autorizzato");
		}

		return tavoloService.listAllElements();
	}

	@GetMapping("/{id}")
	public Tavolo findById(@PathVariable(value = "id", required = true) long id,
			@RequestHeader("authorization") String username) {

		Utente utenteInstance = utenteService.trovaByUsername(username);

		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))
				&& !utenteInstance.getRuoli().contains(ruoloService.findById(3L))) {
			throw new UnouthorizedException("Utente non autorizzato");
		}

		Tavolo tavolo = tavoloService.caricaSingoloElemento(id);

		if (tavolo == null)
			throw new TavoloNotFoundException("Tavolo not found con id: " + id);

		return tavolo;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Tavolo createNew(@Valid @RequestBody Tavolo tavoloInput, @RequestHeader("authorization") String username) {

		Utente utenteInstance = utenteService.trovaByUsername(username);

		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))
				&& !utenteInstance.getRuoli().contains(ruoloService.findById(3L))) {
			throw new UnouthorizedException("Utente non autorizzato");
		}

		return tavoloService.inserisciNuovo(tavoloInput);
	}

	@PutMapping("/{id}")
	public Tavolo update(@Valid @RequestBody Tavolo tavoloInput, @PathVariable(required = true) Long id,
			@RequestHeader("authorization") String username) {

		Utente utenteInstance = utenteService.trovaByUsername(username);

		Tavolo tavolo = tavoloService.caricaSingoloElemento(id);

		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))
				&& !tavolo.getUtenteCreazione().equals(utenteInstance)) {
			throw new UnouthorizedException("Utente non autorizzato");
		}

		if (tavolo == null)
			throw new TavoloNotFoundException("Tavolo not found con id: " + id);

		// controllo se il tavolo è occupato
		if (!tavolo.getUtenti().isEmpty())
			throw new UnouthorizedException("Impossibile modificare, il tavolo è ancora pieno");

		tavoloInput.setId(id);
		return tavoloService.aggiorna(tavoloInput);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable(required = true) Long id, @RequestHeader("authorization") String username) {

		Utente utenteInstance = utenteService.trovaByUsername(username);

		Tavolo tavolo = tavoloService.caricaSingoloElemento(id);

		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))
				&& !tavolo.getUtenteCreazione().equals(utenteInstance)) {
			throw new UnouthorizedException("Utente non autorizzato");
		}

		if (tavolo == null)
			throw new TavoloNotFoundException("Tavolo not found con id: " + id);

		// controllo se il tavolo è occupato
		if (!tavolo.getUtenti().isEmpty())
			throw new UnouthorizedException("Impossibile eliminare, il tavolo è ancora pieno");

		tavoloService.rimuovi(tavolo);
	}

	@PostMapping("/search")
	public List<Tavolo> search(@RequestBody Tavolo example, @RequestHeader("authorization") String username) {

		Utente utenteInstance = utenteService.trovaByUsername(username);

		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))
				&& !utenteInstance.getRuoli().contains(ruoloService.findById(3L))) {
			throw new UnouthorizedException("Utente non autorizzato");
		}

		return tavoloService.findByExample(example);
	}

}
