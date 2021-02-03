package com.nubefact.ose.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.nubefact.ose.dao.IMigradoDAO;
import com.nubefact.ose.entity.Migrado;

@Repository
public class MigradoDAO implements IMigradoDAO {

	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Migrado> getRegistros(Date fechaInicio, Date fechaFin) {
		List<?> list = entityManager
				.createQuery("SELECT DISTINCT(m) FROM Migrado m JOIN FETCH m.ticket WHERE m.fechaRecepcionXml >= :fechaInicio and m.fechaRecepcionXml <= :fechaFin " +
						     "AND (cpe = false or cdr_ose = false or cdr_sunat = false) ORDER BY m.id")
				.setParameter("fechaInicio", fechaInicio)
				.setParameter("fechaFin", fechaFin)
				.getResultList();
		if (!list.isEmpty()) {
			return (List<Migrado>) list;
		}
		return new ArrayList<Migrado>();		
	}

	@Transactional
	@Override
	public void update(Migrado migrado) {
		entityManager.merge(migrado);
	}

}
