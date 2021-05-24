package it.prova.pokeronline.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.service.RuoloService;
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

	@Autowired
	private RuoloService ruoloService;

	// utente acquista per se stesso, non per gli altri
	// non passo id in url, (ma nell'header), per avere una delle possibili
	// gestioni differente dalle altre:
	// {id}/*@PathVariable(required = true) Long id,*/
	@PutMapping("/acquista")
	public Utente compraCredito(@RequestBody Double credito,
			@RequestHeader("authorization") String username/*
															 * ,
															 * 
															 * @RequestHeader("utenteId") String utenteId
															 */) {

		Utente utenteInstance = utenteService.trovaByUsername(username);

		if (utenteInstance == null)
			throw new UtenteNotFoundException("Utente not found con id: " + utenteInstance);

		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))
				&& !utenteInstance.getRuoli().contains(ruoloService.findById(2L))
				&& !utenteInstance.getRuoli().contains(ruoloService.findById(3L))) {
			throw new UnouthorizedException("Utente non autorizzato");
		}

//		Utente utente = utenteService.caricaSingoloElemento(Long.parseLong(utenteId));

		Double creditoUtente = utenteInstance.getCredito();

		utenteInstance.setCredito(creditoUtente + credito);
		return utenteService.aggiorna(utenteInstance);
	}

	@PostMapping("/last")
	public List<Tavolo> lastGame(
			@RequestHeader("authorization") String username/*
															 * ,
															 * 
															 * @RequestHeader("utenteId") String utenteId
															 */) {

		Utente utenteInstance = utenteService.trovaByUsername(username);

		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))
				&& !utenteInstance.getRuoli().contains(ruoloService.findById(2L))
				&& !utenteInstance.getRuoli().contains(ruoloService.findById(3L))) {
			throw new UnouthorizedException("Utente non autorizzato");
		}

//		Utente utente = utenteService.caricaSingoloElemento(Long.parseLong(utenteId));

		Tavolo tavoloExample = new Tavolo();
		tavoloExample.getUtenti().add(utenteInstance);

		return tavoloService.findByExample(tavoloExample);
	}

	@PostMapping("/search")
	public List<Tavolo> ricercaTavolo(@RequestHeader("authorization") String username) {

		Utente utenteInstance = utenteService.trovaByUsername(username);

		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))
				&& !utenteInstance.getRuoli().contains(ruoloService.findById(2L))
				&& !utenteInstance.getRuoli().contains(ruoloService.findById(3L))) {
			throw new UnouthorizedException("Utente non autorizzato");
		}

		// cerco tavolo per esperienza
//		Utente utente = utenteService.caricaSingoloElemento(Long.parseLong(utenteId));
		Double esperienza = utenteInstance.getEsperienzaAccumulata();

		return tavoloService.trovaTavoliByEsperienza(esperienza);
	}

	@PostMapping("/unisciti/{id}")
	public String uniscitiTavolo(@PathVariable(required = true) Long id,
			@RequestHeader("authorization") String username) {

		String messaggio;

		Utente utenteInstance = utenteService.trovaByUsername(username);

		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))
				&& !utenteInstance.getRuoli().contains(ruoloService.findById(2L))
				&& !utenteInstance.getRuoli().contains(ruoloService.findById(3L))) {
			throw new UnouthorizedException("Utente non autorizzato");
		}

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
			return messaggio;
		} else {
			messaggio = "Sei già unito a questo tavolo: " + tavolo.getDenominazione();
			return messaggio;
		}

	}

	@PostMapping("/gioca/{id}")
	public String giocaPartita(@PathVariable(required = true) Long id,
			@RequestHeader("authorization") String username) {

		String messaggio;

		Utente utenteInstance = utenteService.trovaByUsername(username);

		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))
				&& !utenteInstance.getRuoli().contains(ruoloService.findById(2L))
				&& !utenteInstance.getRuoli().contains(ruoloService.findById(3L))) {
			throw new UnouthorizedException("Utente non autorizzato");
		}

		Tavolo tavolo = tavoloService.caricaSingoloElemento(id);

		if (!tavolo.getUtenti().contains(utenteInstance)) {
			messaggio = "Attenzione!! Prima di giocare è necessario unirsi al tavolo";
			return messaggio;
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
				tavolo.getUtenti().remove(utenteInstance);
				tavoloService.aggiorna(tavolo);

				// aggiorno l'esperienza
				utenteInstance.setEsperienzaAccumulata(utenteInstance.getEsperienzaAccumulata() + 1);
				utenteService.aggiorna(utenteInstance);

				messaggio = "Hai perso: " + tot
						+ " Non hai sufficiente credito per giocare a questo tavolo, per favore, ricarica";
			} else {
				// se può ancora giocare, allora informo solo della sconfitta
				messaggio = "Hai perso: " + tot;
			}
		} else {
			// se ha vinto
			messaggio = "Complimenti! Hai vinto:" + tot + " Non dimenticare di dare la mancia al croupier!";
		}
		return messaggio;
	}

	@PostMapping("/abbandona/{id}")
	public String abbandonaPartita(@PathVariable(required = true) Long id,
			@RequestHeader("authorization") String username) {

		Utente utenteInstance = utenteService.trovaByUsername(username);

		if (!utenteInstance.getRuoli().contains(ruoloService.findById(1L))
				&& !utenteInstance.getRuoli().contains(ruoloService.findById(2L))
				&& !utenteInstance.getRuoli().contains(ruoloService.findById(3L))) {
			throw new UnouthorizedException("Utente non autorizzato");
		}

		// disaccoppio tavolo da utente
		utenteInstance.setTavolo(null);

		// aggiorno l'esperienza
		utenteInstance.setEsperienzaAccumulata(utenteInstance.getEsperienzaAccumulata() + 1);
		utenteService.aggiorna(utenteInstance);

		String messaggio = "Uscita dal tavolo avvenuta con successo";

		return messaggio;

	}
}
