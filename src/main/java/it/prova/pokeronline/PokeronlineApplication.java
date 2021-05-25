package it.prova.pokeronline;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.prova.pokeronline.model.Authority;
import it.prova.pokeronline.model.AuthorityName;
import it.prova.pokeronline.model.User;
import it.prova.pokeronline.security.repository.AuthorityRepository;
import it.prova.pokeronline.service.UtenteService;

@SpringBootApplication
public class PokeronlineApplication {

	@Autowired
	AuthorityRepository authorityRepository;

	@Autowired
	private UtenteService utenteService;

	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(PokeronlineApplication.class, args);
	}

	@Bean
	public CommandLineRunner initUsers() {
		return (args) -> {
			// Ora la parte di sicurezza
			User user = utenteService.trovaByUsername("admin");

			if (user == null) {

				/**
				 * Inizializzo i dati del mio test
				 */

				Authority authorityAdmin = new Authority();
				authorityAdmin.setName(AuthorityName.ROLE_ADMIN);
				authorityAdmin = authorityRepository.save(authorityAdmin);

				Authority authoritySpecialUser = new Authority();
				authoritySpecialUser.setName(AuthorityName.ROLE_SPECIAL_PLAYER);
				authoritySpecialUser = authorityRepository.save(authoritySpecialUser);

				Authority authorityUser = new Authority();
				authorityUser.setName(AuthorityName.ROLE_PLAYER);
				authorityUser = authorityRepository.save(authorityUser);

				List<Authority> authorities = Arrays
						.asList(new Authority[] { authorityAdmin, authoritySpecialUser, authorityUser });

				user = new User();
				user.setAuthorities(authorities);
				user.setEnabled(true);
				user.setUsername("admin");
				user.setPassword(passwordEncoder.encode("admin"));
				user.setEmail("admin@example.com");
				user.setCognome("sblocco");
				user.setNome("rocco");
				user.setCredito(0.0);
				user.setEsperienzaAccumulata(0.0);
				user.setDataRegistrazione(new Date());

				user = utenteService.inserisciNuovo(user);

			}

			User specialUser = utenteService.trovaByUsername("specialUser");

			if (specialUser == null) {

				/**
				 * Inizializzo i dati del mio test
				 */

				Authority authoritySpecialUser = authorityRepository.findByName(AuthorityName.ROLE_SPECIAL_PLAYER);
				if (authoritySpecialUser == null) {
					authoritySpecialUser = new Authority();
					authoritySpecialUser.setName(AuthorityName.ROLE_SPECIAL_PLAYER);
					authoritySpecialUser = authorityRepository.save(authoritySpecialUser);
				}

				List<Authority> authorities = Arrays.asList(new Authority[] { authoritySpecialUser });

				specialUser = new User();
				specialUser.setAuthorities(authorities);
				specialUser.setEnabled(true);
				specialUser.setUsername("specialUser");
				specialUser.setPassword(passwordEncoder.encode("specialUser"));
				specialUser.setEmail("specialUser@example.com");
				specialUser.setCognome("nesi");
				specialUser.setNome("piermauro");
				specialUser.setCredito(0.0);
				specialUser.setEsperienzaAccumulata(0.0);
				specialUser.setDataRegistrazione(new Date());

				specialUser = utenteService.inserisciNuovo(specialUser);

			}

			User commonUser = utenteService.trovaByUsername("commonUser");

			if (commonUser == null) {

				/**
				 * Inizializzo i dati del mio test
				 */

				Authority authorityUser = authorityRepository.findByName(AuthorityName.ROLE_PLAYER);
				if (authorityUser == null) {
					authorityUser = new Authority();
					authorityUser.setName(AuthorityName.ROLE_PLAYER);
					authorityUser = authorityRepository.save(authorityUser);
				}

				List<Authority> authorities = Arrays.asList(new Authority[] { authorityUser });

				commonUser = new User();
				commonUser.setAuthorities(authorities);
				commonUser.setEnabled(true);
				commonUser.setUsername("commonUser");
				commonUser.setPassword(passwordEncoder.encode("commonUser"));
				commonUser.setEmail("commonUser@example.com");
				commonUser.setCognome("ross");
				commonUser.setNome("monica");
				commonUser.setCredito(0.0);
				commonUser.setEsperienzaAccumulata(0.0);
				commonUser.setDataRegistrazione(new Date());

				commonUser = utenteService.inserisciNuovo(commonUser);

			}
		};
	}

}
