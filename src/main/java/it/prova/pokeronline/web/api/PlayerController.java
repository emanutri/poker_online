package it.prova.pokeronline.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.User;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;
import it.prova.pokeronline.web.api.exception.NoCreditException;
import it.prova.pokeronline.web.api.exception.UnouthorizedException;
import it.prova.pokeronline.web.api.exception.UtenteNotFoundException;

@RestController
@RequestMapping("api/player")
public class PlayerController {

	@Autowired
	private TavoloService tavoloService;

	@Autowired
	private UtenteService utenteService;

//	@Autowired
//	private RuoloService ruoloService;

	// utente acquista per se stesso, non per gli altri
	@PutMapping("/acquista")
	public ResponseEntity<User> compraCredito(@RequestBody Double credito, @RequestHeader("authorization") String username) {

		User utenteInstance = utenteService.trovaByUsername(username);

		if (utenteInstance == null)
			throw new UtenteNotFoundException("Utente not found con id: " + utenteInstance);

//		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))
//				&& !utenteInstance.getRuoli().contains(ruoloService.findById(2L))
//				&& !utenteInstance.getRuoli().contains(ruoloService.findById(3L))) {
//			throw new UnouthorizedException("Utente non autorizzato");
//		}

		Double creditoUtente = utenteInstance.getCredito();

		utenteInstance.setCredito(creditoUtente + credito);
		return ResponseEntity.ok().body(utenteService.aggiorna(utenteInstance));
	}

	@GetMapping("/last")
	public ResponseEntity<List<Tavolo>> lastGame(@RequestHeader("authorization") String username) {

		User utenteInstance = utenteService.trovaByUsername(username);

//		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))
//				&& !utenteInstance.getRuoli().contains(ruoloService.findById(2L))
//				&& !utenteInstance.getRuoli().contains(ruoloService.findById(3L))) {
//			throw new UnouthorizedException("Utente non autorizzato");
//		}

		Tavolo tavoloExample = new Tavolo();
		tavoloExample.getUtenti().add(utenteInstance);

		return ResponseEntity.ok().body(tavoloService.findByExample(tavoloExample));
	}

	@GetMapping("/search")
	public ResponseEntity<List<Tavolo>> ricercaTavolo(@RequestHeader("authorization") String username) {

		User utenteInstance = utenteService.trovaByUsername(username);

//		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))
//				&& !utenteInstance.getRuoli().contains(ruoloService.findById(2L))
//				&& !utenteInstance.getRuoli().contains(ruoloService.findById(3L))) {
//			throw new UnouthorizedException("Utente non autorizzato");
//		}

		// cerco tavolo per esperienza
		Double esperienza = utenteInstance.getEsperienzaAccumulata();

		return ResponseEntity.ok().body(tavoloService.trovaTavoliByEsperienza(esperienza));
	}

	@PostMapping("/unisciti/{id}")
	public ResponseEntity<String> uniscitiTavolo(@PathVariable(required = true) Long id,
			@RequestHeader("authorization") String username) {

		String messaggio;

		User utenteInstance = utenteService.trovaByUsername(username);

//		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))
//				&& !utenteInstance.getRuoli().contains(ruoloService.findById(2L))
//				&& !utenteInstance.getRuoli().contains(ruoloService.findById(3L))) {
//			throw new UnouthorizedException("Utente non autorizzato");
//		}

		Tavolo tavolo = tavoloService.caricaSingoloElemento(id);

		if (utenteInstance.getCredito() < tavolo.getCifraMinima()) {
			throw new NoCreditException(
					"Non hai sufficiente credito per giocare a questo tavolo, per favore, ricarica");
		}
		if (utenteInstance.getEsperienzaAccumulata() < tavolo.getEsperienzaMin()) {
			throw new UnouthorizedException("Non hai sufficiente esperienza per giocare a questo tavolo");
		}

		if (!tavolo.getUtenti().contains(utenteInstance)) {

			utenteInstance.setTavolo(tavolo);
			utenteService.aggiorna(utenteInstance);

			messaggio = "Sei entrato con successo al tavolo: " + tavolo.getDenominazione();
			return ResponseEntity.ok().body(messaggio);
		} else {
			messaggio = "Sei già unito a questo tavolo: " + tavolo.getDenominazione();
			return ResponseEntity.badRequest().body(messaggio);
		}

	}

	@PostMapping("/gioca/{id}")
	public ResponseEntity<String> giocaPartita(@PathVariable(required = true) Long id,
			@RequestHeader("authorization") String username) {

		String messaggio;

		User utenteInstance = utenteService.trovaByUsername(username);

//		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))
//				&& !utenteInstance.getRuoli().contains(ruoloService.findById(2L))
//				&& !utenteInstance.getRuoli().contains(ruoloService.findById(3L))) {
//			throw new UnouthorizedException("Utente non autorizzato");
//		}

		Tavolo tavolo = tavoloService.caricaSingoloElemento(id);

		if (!tavolo.getUtenti().contains(utenteInstance)) {
			messaggio = "Attenzione!! Prima di giocare è necessario unirsi al tavolo";
			return ResponseEntity.badRequest().body(messaggio);
		}

		if (utenteInstance.getCredito() < tavolo.getCifraMinima()) {
			messaggio = "Attenzione!! Non hai sufficiente credito per giocare a questo tavolo, per favore, ricarica.";
			return ResponseEntity.badRequest().body(messaggio);
		}

		// gioca
		// se segno >=0.5 segno positivo, negativo altrimenti.
		Double segno = Math.random();
		if (segno >= 0.5) {
			segno = 1.0;
		} else {
			segno = -1.0;
		}
		Integer somma = (int) (Math.random() * 1000);
		Double tot = segno * somma;

		// modifico il credito e salvo il credito residuo utente
		Double credito = utenteInstance.getCredito();
		credito = credito + tot;
		utenteInstance.setCredito(credito);
		utenteService.aggiorna(utenteInstance);

		// se ha perso
		if (tot <= 0) {

			if (utenteInstance.getCredito() < tavolo.getCifraMinima()) {
				// se l'utente ha superato il credito minimo
				// lo mando via dal tavolo
				utenteInstance.setTavolo(null);
				utenteService.aggiorna(utenteInstance);

				// aggiorno l'esperienza
				utenteInstance.setEsperienzaAccumulata(utenteInstance.getEsperienzaAccumulata() + 1);
				utenteService.aggiorna(utenteInstance);

				messaggio = "Hai perso: " + tot
						+ " Non hai sufficiente credito per giocare a questo tavolo, per favore, ricarica. Uscita in corso...";
			} else {
				// se può ancora giocare, allora informo solo della sconfitta
				messaggio = "Hai perso: " + tot;
			}
		} else {
			// se ha vinto
			messaggio = "Complimenti! Hai vinto:" + tot + " Non dimenticare di dare la mancia al croupier!";
		}
		return ResponseEntity.ok().body(messaggio);
	}

	@PutMapping("/abbandona/{id}")
	public ResponseEntity<String> abbandonaPartita(@PathVariable(required = true) Long id,
			@RequestHeader("authorization") String username) {

		User utenteInstance = utenteService.trovaByUsername(username);

//		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))
//				&& !utenteInstance.getRuoli().contains(ruoloService.findById(2L))
//				&& !utenteInstance.getRuoli().contains(ruoloService.findById(3L))) {
//			throw new UnouthorizedException("Utente non autorizzato");
//		}

		// disaccoppio tavolo da utente
		utenteInstance.setTavolo(null);

		// aggiorno l'esperienza
		utenteInstance.setEsperienzaAccumulata(utenteInstance.getEsperienzaAccumulata() + 1);
		utenteService.aggiorna(utenteInstance);

		String messaggio = "Uscita dal tavolo avvenuta con successo";

		return  ResponseEntity.ok().body(messaggio);

	}
}
