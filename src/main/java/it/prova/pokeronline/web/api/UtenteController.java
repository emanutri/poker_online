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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.model.User;
import it.prova.pokeronline.service.UtenteService;
import it.prova.pokeronline.web.api.exception.UnouthorizedException;
import it.prova.pokeronline.web.api.exception.UtenteNotFoundException;

@RestController
@RequestMapping("api/user")
public class UtenteController {

	@Autowired
	private UtenteService utenteService;

//	@Autowired
//	private RuoloService ruoloService;

	@GetMapping
	public ResponseEntity<List<User>> getAll(@RequestHeader("authorization") String username) {

		User utenteInstance = utenteService.trovaByUsername(username);

//		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))) {
//			throw new UnouthorizedException("Utente non autorizzato");
//		}

		return ResponseEntity.ok().body(utenteService.listAllElements());
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> findById(@PathVariable(value = "id", required = true) long id,
			@RequestHeader("authorization") String username) {

		User utenteInstance = utenteService.trovaByUsername(username);

		if (utenteInstance == null)
			throw new UtenteNotFoundException("Utente not found con id: " + id);

//		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))) {
//			throw new UnouthorizedException("Utente non autorizzato");
//		}

		return ResponseEntity.ok().body(utenteInstance);
	}

	// gli errori di validazione vengono mostrati con 400 Bad Request ma
	// elencandoli grazie al ControllerAdvice
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<User> createNew(@Valid @RequestBody User utenteInput,
			@RequestHeader("authorization") String username) {

		User utenteInstance = utenteService.trovaByUsername(username);

//		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))) {
//			throw new UnouthorizedException("Utente non autorizzato");
//		}
		return ResponseEntity.ok().body(utenteService.inserisciNuovo(utenteInput));
	}

	@PutMapping("/{id}")
	public ResponseEntity<User> update(@Valid @RequestBody User utenteInput, @PathVariable(required = true) Long id,
			@RequestHeader("authorization") String username) {

		User utenteInstance = utenteService.trovaByUsername(username);

		if (utenteInstance == null)
			throw new UtenteNotFoundException("Utente not found con id: " + id);

//		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))) {
//			throw new UnouthorizedException("Utente non autorizzato");
//		}

		utenteInput.setId(id);

		return ResponseEntity.ok().body(utenteService.aggiorna(utenteInput));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable(required = true) Long id, @RequestHeader("authorization") String username) {

		User utenteInstance = utenteService.trovaByUsername(username);

		if (utenteInstance == null)
			throw new UtenteNotFoundException("Utente not found con id: " + id);

//		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))) {
//			throw new UnouthorizedException("Utente non autorizzato");
//		}

//		utenteInstance.setStato(StatoUtente.DISABILITATO);
		utenteInstance.setEnabled(false);
		utenteInstance.setId(id);
		utenteService.aggiorna(utenteInstance);
	}

	@PostMapping("/search")
	public ResponseEntity<List<User>> search(@RequestBody User example,
			@RequestHeader("authorization") String username) {

		User utenteInstance = utenteService.trovaByUsername(username);

//		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))) {
//			throw new UnouthorizedException("Utente non autorizzato");
//		}

		return ResponseEntity.ok().body(utenteService.findByExample(example));
	}

	@PutMapping("/credito/{id}")
	public ResponseEntity<User> caricaCredito(@RequestBody Double credito, @PathVariable(required = true) Long id,
			@RequestHeader("authorization") String username) {

		User utenteInstance = utenteService.trovaByUsername(username);

		if (utenteInstance == null)
			throw new UtenteNotFoundException("Utente not found con id: " + id);

//		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))) {
//			throw new UnouthorizedException("Utente non autorizzato");
//		}

		Double creditoUtente = utenteInstance.getCredito();

		utenteInstance.setCredito(creditoUtente + credito);

		return ResponseEntity.ok().body(utenteService.aggiorna(utenteInstance));
	}
}
