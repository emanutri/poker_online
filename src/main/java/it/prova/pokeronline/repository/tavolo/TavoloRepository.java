package it.prova.pokeronline.repository.tavolo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.prova.pokeronline.model.Tavolo;

public interface TavoloRepository extends CrudRepository<Tavolo, Long>, CustomTavoloRepository {

	@Query("select t from Tavolo t where t.esperienzaMin <= ?1")
	List<Tavolo> findAllTavoloByEsperienza(Double esperienza);

}
