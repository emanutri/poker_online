package it.prova.pokeronline.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.prova.pokeronline.model.User;
import it.prova.pokeronline.repository.utente.CustomUserRepository;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
	Optional<User> findByUsername(String username);

}