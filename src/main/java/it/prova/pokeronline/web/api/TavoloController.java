package it.prova.pokeronline.web.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;
import it.prova.pokeronline.web.api.exception.UnouthorizedException;

@RestController
@RequestMapping("api/tavolo")
public class TavoloController {

	@Autowired
	private TavoloService tavoloService;

	@GetMapping
	public ResponseEntity<List<Tavolo>> getAll() {

		return ResponseEntity.ok().body(tavoloService.listAllElements());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Tavolo> findById(@PathVariable(value = "id", required = true) long id) {

		Tavolo tavolo = tavoloService.caricaSingoloElemento(id);

		if (tavolo == null)
			throw new TavoloNotFoundException("Tavolo not found con id: " + id);

		return ResponseEntity.ok().body(tavolo);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Tavolo> createNew(@Valid @RequestBody Tavolo tavoloInput) {

		return ResponseEntity.ok().body(tavoloService.inserisciNuovo(tavoloInput));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Tavolo> update(@Valid @RequestBody Tavolo tavoloInput,
			@PathVariable(required = true) Long id) {

		Tavolo tavolo = tavoloService.caricaSingoloElemento(id);

		if (tavolo == null)
			throw new TavoloNotFoundException("Tavolo not found con id: " + id);

		// controllo se il tavolo è occupato
		if (!tavolo.getUtenti().isEmpty())
			throw new UnouthorizedException("Impossibile modificare, il tavolo è ancora pieno");

		tavoloInput.setId(id);

		return ResponseEntity.ok().body(tavoloService.aggiorna(tavoloInput));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable(required = true) Long id) {

		Tavolo tavolo = tavoloService.caricaSingoloElemento(id);

		if (tavolo == null)
			throw new TavoloNotFoundException("Tavolo not found con id: " + id);

		// controllo se il tavolo è occupato
		if (!tavolo.getUtenti().isEmpty())
			throw new UnouthorizedException("Impossibile eliminare, il tavolo è ancora pieno");

		tavoloService.rimuovi(tavolo);
	}

	@PostMapping("/search")
	public ResponseEntity<List<Tavolo>> search(@RequestBody Tavolo example) {

		return ResponseEntity.ok().body(tavoloService.findByExample(example));
	}

}
