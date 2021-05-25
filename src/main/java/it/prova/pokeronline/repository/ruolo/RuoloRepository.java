package it.prova.pokeronline.repository.ruolo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.prova.pokeronline.model.Authority;

public interface RuoloRepository extends CrudRepository<Authority, Long> {

	Optional<Authority> findById(Long id);
}
