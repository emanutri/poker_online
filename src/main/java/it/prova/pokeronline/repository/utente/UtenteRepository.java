package it.prova.pokeronline.repository.utente;

import org.springframework.data.repository.CrudRepository;

import it.prova.pokeronline.model.Utente;

public interface UtenteRepository extends CrudRepository<Utente, Long>, CustomUtenteRepository {

}
