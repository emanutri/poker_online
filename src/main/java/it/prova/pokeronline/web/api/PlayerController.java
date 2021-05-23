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
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;
import it.prova.pokeronline.web.api.exception.NoCreditException;
import it.prova.pokeronline.web.api.exception.UnouthorizedException;
import it.prova.pokeronline.web.api.exception.UtenteNotFoundException;
import it.prova.pokeronline.web.api.exception.YouLostException;
import it.prova.pokeronline.web.api.exception.YouWinException;

@RestController
@RequestMapping("api/player")
public class PlayerController {

	@Autowired
	private TavoloService tavoloService;

	@Autowired
	private UtenteService utenteService;

	// utente acquista per se stesso, non per gli altri
	// non passo id in url, (ma nell'header), per avere una delle possibili
	// gestioni differente dalle altre:
	// {id}/*@PathVariable(required = true) Long id,*/
	@PutMapping("/acquista")
	public Utente compraCredito(@RequestBody Double credito, @RequestHeader("authorization") String utenteRole,
			@RequestHeader("utenteId") String utenteId) {

		if (!utenteRole.equals("admin") && !utenteRole.equals("special player")
				&& !utenteRole.equals("simple player")) {
			throw new UnouthorizedException("Utente non autorizzato");
		}

		Utente utente = utenteService.caricaSingoloElemento(Long.parseLong(utenteId));

		if (utente == null)
			throw new UtenteNotFoundException("Utente not found con id: " + Long.parseLong(utenteId));

		utente.setCredito(credito);
		return utenteService.aggiorna(utente);
	}

	@PostMapping("/last")
	public List<Tavolo> lastGame(@RequestHeader("authorization") String utenteRole,
			@RequestHeader("utenteId") String utenteId) {

		if (!utenteRole.equals("admin") && !utenteRole.equals("special player")
				&& !utenteRole.equals("simple player")) {
			throw new UnouthorizedException("Utente non autorizzato");
		}
		Utente utente = utenteService.caricaSingoloElemento(Long.parseLong(utenteId));

		Tavolo tavoloExample = new Tavolo();
		tavoloExample.getUtenti().add(utente);

		return tavoloService.findByExample(tavoloExample);
	}

	@PostMapping("/search")
	public List<Tavolo> ricercaTavolo(@RequestHeader("authorization") String utenteRole,
			@RequestHeader("utenteId") String utenteId) {

		if (!utenteRole.equals("admin") && !utenteRole.equals("special player")
				&& !utenteRole.equals("simple player")) {
			throw new UnouthorizedException("Utente non autorizzato");
		}

		// cerco tavolo per esperienza
		Utente utente = utenteService.caricaSingoloElemento(Long.parseLong(utenteId));
		Double esperienza = utente.getEsperienzaAccumulata();
		Tavolo tavoloExample = new Tavolo();
		tavoloExample.setEsperienzaMin(esperienza);

//		System.out.println(tavoloExample+"AAAAAAAAAAAAAAAAAAAA"+tavoloService.findByExample(tavoloExample));

		return tavoloService.findByExample(tavoloExample);
	}

	@PostMapping("/gioca/{id}")
	public String giocaPartita(@PathVariable(required = true) Long id, @RequestHeader("authorization") String utenteRole,
			@RequestHeader("utenteId") String utenteId) {
		
		String messaggio;
		
		if (!utenteRole.equals("admin") && !utenteRole.equals("special player")
				&& !utenteRole.equals("simple player")) {
			throw new UnouthorizedException("Utente non autorizzato");
		}

		Utente utente = utenteService.caricaSingoloElemento(Long.parseLong(utenteId));

		Tavolo tavolo = tavoloService.caricaSingoloElemento(id);

		if (utente.getCredito() < tavolo.getCifraMinima()) {
			throw new NoCreditException(
					"Non hai sufficiente credito per giocare a questo tavolo, per favore, ricarica");
		}

		// dopo aver controllato se ha abbastanza credito, lo aggiungo al tavolo
		if (!tavolo.getUtenti().contains(utente)) {
			tavolo.getUtenti().add(utente);
			tavoloService.aggiorna(tavolo);
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
		System.out.println("PRIMAAA" + utente.getCredito());
		System.out.println(segno + "---" + somma + "---" + tot);
		// modifico il credito e salvo il credito residuo utente
		Double credito = utente.getCredito();
		credito = credito + tot;
		utente.setCredito(credito);
		utenteService.aggiorna(utente);
		System.out.println("DOPOOO" + utente.getCredito());

		// se ha perso
		if (tot <= 0) {

			if (utente.getCredito() < tavolo.getCifraMinima()) {
				// se l'utente ha superato il credito minimo
				// lo mando via dal tavolo
				tavolo.getUtenti().remove(utente);
				tavoloService.aggiorna(tavolo);

				// aggiorno l'esperienza
				utente.setEsperienzaAccumulata(utente.getEsperienzaAccumulata() + 1);
				utenteService.aggiorna(utente);

				messaggio ="Hai perso: "+tot+" Non hai sufficiente credito per giocare a questo tavolo, per favore, ricarica";
//				// l'ancio l'eccezione per informarlo
//				throw new NoCreditException("Hai perso:" + tot
//						+ " Non hai sufficiente credito per giocare a questo tavolo, per favore, ricarica");
			} else {
				// se può ancora giocare, allora informo solo della sconfitta
				messaggio = "Hai perso: "+tot;
//				throw new YouLostException("Hai perso: " + tot);
			}
		} else {
			// se ha vinto
			messaggio = "Complimenti! Hai vinto:" + tot + " Non dimenticare di dare la mancia al croupier!";
//			// l'ancio l'eccezione per informarlo
//			throw new YouWinException(
//					"Complimenti! Hai vinto:" + tot + " Non dimenticare di dare la mancia al croupier!");
		}
		return messaggio;
	}
}
