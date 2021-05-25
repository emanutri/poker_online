package it.prova.pokeronline.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.prova.pokeronline.model.Authority;
import it.prova.pokeronline.model.AuthorityName;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	Authority findByName(AuthorityName name);

}