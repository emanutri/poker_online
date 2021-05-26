package it.prova.pokeronline.security.repository;

import java.util.List;

import it.prova.pokeronline.model.Authority;
import it.prova.pokeronline.model.User;

public interface CustomUserRepository {

	void aggiungiRuolo(User utenteEsistente, Authority ruoloInstance);

	List<User> findByExample(User example);

}
