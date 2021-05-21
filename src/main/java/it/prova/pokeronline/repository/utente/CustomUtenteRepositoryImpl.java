package it.prova.pokeronline.repository.utente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.model.Ruolo;
import it.prova.pokeronline.model.Utente;

public class CustomUtenteRepositoryImpl implements CustomUtenteRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void aggiungiRuolo(Utente utenteEsistente, Ruolo ruoloInstance) {
		
		//ruoloInstance = entityManager.merge(ruoloInstance);
		//utenteEsistente = entityManager.merge(utenteEsistente);
		

		utenteEsistente.getRuoli().add(ruoloInstance);
	}
	
	@Override
	public List<Utente> findByExample(Utente example) {
		Map<String, Object> paramaterMap = new HashMap<String, Object>();
		List<String> whereClauses = new ArrayList<String>();

		StringBuilder queryBuilder = new StringBuilder(
				"select u from Utente left join fetch u.tavolo t left join fetch u.ruoli r where 1 = 1 ");

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
		if (example.getStato() != null) {
			whereClauses.add(" u.stato  >= :stato ");
			paramaterMap.put("cifraMinima", example.getStato());
		}
		if (example.getEsperienzaAccumulata() != null) {
			whereClauses.add("u.esperienzaAccumulata >= :esperienzaAccumulata ");
			paramaterMap.put("esperienzaAccumulata", example.getEsperienzaAccumulata());
		}
		if (example.getCredito() != null) {
			whereClauses.add("u.credito >= :credito ");
			paramaterMap.put("credito", example.getCredito());
		}
		if (example.getRuoli() != null) {
			whereClauses.add(" r =:ruoli ");
			paramaterMap.put("ruoli", example.getRuoli());
		}
		if (example.getTavolo() != null) {
			whereClauses.add(" t =:tavolo ");
			paramaterMap.put("tavolo", example.getTavolo());
		}

		queryBuilder.append(!whereClauses.isEmpty() ? " and " : "");
		queryBuilder.append(StringUtils.join(whereClauses, " and "));
		TypedQuery<Utente> typedQuery = entityManager.createQuery(queryBuilder.toString(), Utente.class);

		for (String key : paramaterMap.keySet()) {
			typedQuery.setParameter(key, paramaterMap.get(key));
		}

		return typedQuery.getResultList();
	}
}
