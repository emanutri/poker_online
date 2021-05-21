package it.prova.pokeronline.repository.ruolo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.prova.pokeronline.model.Ruolo;

public interface RuoloRepository extends CrudRepository<Ruolo, Long> {

	//Optional<Ruolo> findById(Long id);
}
