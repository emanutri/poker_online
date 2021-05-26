package it.prova.pokeronline.security.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.model.Authority;
import it.prova.pokeronline.model.User;

public class CustomUserRepositoryImpl implements CustomUserRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void aggiungiRuolo(User utenteEsistente, Authority ruoloInstance) {
		
		//ruoloInstance = entityManager.merge(ruoloInstance);
		//utenteEsistente = entityManager.merge(utenteEsistente);
		

		utenteEsistente.getAuthorities().add(ruoloInstance);
	}
	
	@Override
	public List<User> findByExample(User example) {
		Map<String, Object> paramaterMap = new HashMap<String, Object>();
		List<String> whereClauses = new ArrayList<String>();

		StringBuilder queryBuilder = new StringBuilder(
				"select u from User u left join fetch u.tavolo t left join fetch u.authorities a where 1 = 1 ");

		if (StringUtils.isNotEmpty(example.getNome())) {
			whereClauses.add(" u.nome like :nome ");
			paramaterMap.put("nome", "%" + example.getNome() + "%");
		}
		if (StringUtils.isNotEmpty(example.getCognome())) {
			whereClauses.add(" u.cognome like :cognome ");
			paramaterMap.put("cognome", "%" + example.getCognome() + "%");
		}
		if (StringUtils.isNotEmpty(example.getUsername())) {
			whereClauses.add(" u.username like :username ");
			paramaterMap.put("username", "%" + example.getUsername() + "%");
		}
		if (example.getDataRegistrazione() != null) {
			whereClauses.add(" u.dataRegistrazione  >= :dataRegistrazione ");
			paramaterMap.put("esperienzaMin", example.getDataRegistrazione());
		}
		if (example.getEnabled() != null) {
			whereClauses.add(" u.enabled  >= :enabled ");
			paramaterMap.put("enabled", example.getEnabled());
		}
		if (example.getEsperienzaAccumulata() != null) {
			whereClauses.add("u.esperienzaAccumulata >= :esperienzaAccumulata ");
			paramaterMap.put("esperienzaAccumulata", example.getEsperienzaAccumulata());
		}
		if (example.getCredito() != null) {
			whereClauses.add("u.credito >= :credito ");
			paramaterMap.put("credito", example.getCredito());
		}
		if (example.getAuthorities() != null && !example.getAuthorities().isEmpty()) {
			whereClauses.add(" a in :authorities ");
			paramaterMap.put("authorities", example.getAuthorities());
		}
		if (example.getTavolo() != null) {
			whereClauses.add(" t =:tavolo ");
			paramaterMap.put("tavolo", example.getTavolo());
		}

		queryBuilder.append(!whereClauses.isEmpty() ? " and " : "");
		queryBuilder.append(StringUtils.join(whereClauses, " and "));
		TypedQuery<User> typedQuery = entityManager.createQuery(queryBuilder.toString(), User.class);

		for (String key : paramaterMap.keySet()) {
			typedQuery.setParameter(key, paramaterMap.get(key));
		}

		return typedQuery.getResultList();
	}
}
