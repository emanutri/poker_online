package it.prova.pokeronline.web.api;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.model.Ruolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.service.RuoloService;
import it.prova.pokeronline.service.UtenteService;
import it.prova.pokeronline.web.api.exception.UtenteNotFoundException;

@RestController
@RequestMapping("api/utente")
public class UtenteController {

	@Autowired
	private UtenteService utenteService;
	
	@Autowired
	private RuoloService ruoloService;

	@GetMapping
	public List<Utente> getAll() {
		return utenteService.listAllElements();
	}

	@GetMapping("/{id}")
	public Utente findById(@PathVariable(value = "id", required = true) long id) {
		Utente utente = utenteService.caricaSingoloElemento(id);

		if (utente == null)
			throw new UtenteNotFoundException("Utente not found con id: " + id);

		return utente;
	}

	// gli errori di validazione vengono mostrati con 400 Bad Request ma
	// elencandoli grazie al ControllerAdvice
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Utente createNew(@Valid @RequestBody Utente utenteInput) {
		
		if(utenteInput.getRuoli() != null) {
			for(Ruolo ruoloInstance: utenteInput.getRuoli())
				utenteService.aggiungiRuolo(utenteInput, ruoloInstance);
			
		}
		return utenteService.inserisciNuovo(utenteInput);
	}

	@PutMapping("/{id}")
	public Utente update(@Valid @RequestBody Utente utenteInput, @PathVariable(required = true) Long id) {
		Utente utente = utenteService.caricaSingoloElemento(id);

		if (utente == null)
			throw new UtenteNotFoundException("Utente not found con id: " + id);

		utenteInput.setId(id);
		return utenteService.aggiorna(utenteInput);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable(required = true) Long id) {
		Utente utente = utenteService.caricaSingoloElemento(id);

		if (utente == null)
			throw new UtenteNotFoundException("Utente not found con id: " + id);

		utenteService.rimuovi(utente);
	}

	@PostMapping("/search")
	public List<Utente> search(@RequestBody Utente example) {
		return utenteService.findByExample(example);
	}
}
