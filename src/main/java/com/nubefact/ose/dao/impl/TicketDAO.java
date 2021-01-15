package com.nubefact.ose.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.nubefact.ose.dao.ITicketDAO;
import com.nubefact.ose.entity.Ticket;
import com.nubefact.parametros.EstatusEnvioSunat;
import com.nubefact.ose.utils.OseUtils;

@Repository
public class TicketDAO implements ITicketDAO{
	
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Ticket> getTickets(String numRuc, String nombreDoc) {
		List<?> list = entityManager
				.createQuery("SELECT DISTINCT(t) FROM Ticket t JOIN FETCH t.emisor WHERE t.numRuc=:numRuc AND t.nombreDoc=:nombreDoc")
				.setParameter("numRuc", numRuc)
				.setParameter("nombreDoc", nombreDoc)
				.getResultList();
		
		if (!list.isEmpty()) {
			return (List<Ticket>) list;
		}
		return null;
	}	
	/**
	 * 
	 */
	@Transactional
	public void update(Ticket ticket) {
		entityManager.merge(ticket);
	}
	/**
	 * 
	 */
	public Ticket getTicket(String nroTicket) {
		Ticket ticket = null;
		List<?> list = entityManager
				.createQuery("SELECT DISTINCT(t) FROM Ticket t JOIN FETCH t.emisor WHERE t.nroTicket=:nroTicket")
				.setParameter("nroTicket", nroTicket)
				.getResultList();
		if (!list.isEmpty()) {
			ticket = (Ticket) list.get(0);
		}		
		return ticket;
	}
	/**
	 * 
	 */
	@Transactional
	public void deleteAll(String numRuc)
	{
		entityManager.createQuery("DELETE FROM Ticket WHERE numRuc=:numRuc")
        .setParameter("numRuc", numRuc)
        .executeUpdate();		
	}
	/**
	 * 
	 */
	public Ticket getTicket(String rucComprobante, String tipoComprobante, String serieComprobante,
			String numeroComprobante) {
		if (numeroComprobante.matches("^[0-9]{1,8}$"))
		{
			Integer correlativoInt = Integer.parseInt(numeroComprobante);
			numeroComprobante = correlativoInt.toString();			
		}
		Ticket ticket = null;
		List<?> list = entityManager
				.createQuery("SELECT DISTINCT(t) FROM Ticket t JOIN FETCH t.emisor WHERE t.numRuc=:numRuc and t.codDoc=:codDoc " +
						     "and t.serie=:serie and t.correlativo=:correlativo ORDER BY t.estatus, t.id DESC")
				.setParameter("numRuc", rucComprobante)
				.setParameter("codDoc", tipoComprobante)
				.setParameter("serie", serieComprobante)
				.setParameter("correlativo", numeroComprobante)
				.getResultList();
		if (!list.isEmpty()) {
			ticket = (Ticket) list.get(0);
		}
		return ticket;
	}
	/**
	 * 
	 */
	public Ticket getTicket(UUID uuid) {
		Ticket ticket = null;
		List<?> list = entityManager
				.createQuery("SELECT DISTINCT(t) FROM Ticket t JOIN FETCH t.emisor WHERE t.idUUID=:idUUID")
				.setParameter("idUUID", uuid)
				.getResultList();
		if (!list.isEmpty()) {
			ticket = (Ticket) list.get(0);
		}
		return ticket;
	}
	/**
	 * 
	 */
	public Ticket getTicket(Long idTicket) {
		Ticket ticket = null;
		List<?> list = entityManager
				.createQuery("SELECT t FROM Ticket t JOIN FETCH t.emisor WHERE t.id=:id")
				.setParameter("id", idTicket)
				.getResultList();
		if (!list.isEmpty()) {
			ticket = (Ticket) list.get(0);
		}		
		return ticket;
	}
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Ticket> getTicketsPorEnviar(String ruc) 
	{
		List<?> list = entityManager
				.createQuery("SELECT t FROM Ticket t "
						+ "WHERE t.numRuc=:numruc and t.estatus=:estatus and "
						+ "(enviado_sunat=:pendiente or enviado_sunat=:error_conexion) ORDER BY t.id")
				.setParameter("numruc", ruc)
				.setParameter("estatus", Ticket.ESTATUS_ACEPTADO)
				.setParameter("pendiente", EstatusEnvioSunat.PENDIENTE.ordinal())
				.setParameter("error_conexion", EstatusEnvioSunat.ERROR_CONEXION.ordinal())
				.getResultList();
		
		if (!list.isEmpty()) {
			return (List<Ticket>) list;
		}
		return new ArrayList<Ticket>();
	}
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Ticket> getTicketsSummaryEmision(String rucComprobante, String fechaInicio, String fechaFin) 
	{
		List<?> list = entityManager
				.createQuery("SELECT DISTINCT(t) FROM Ticket t JOIN FETCH t.emisor WHERE t.numRuc=:numRuc AND " +
						     "t.estatus='0' and (t.codDoc='RC' or t.codDoc='RA' or t.codDoc='RR') AND " +
						     "t.fechaEmision >= :fechaInicio and t.fechaEmision <= :fechaFin " +
						     "ORDER BY t.id")
				.setParameter("numRuc", rucComprobante)
				.setParameter("fechaInicio", fechaInicio)
				.setParameter("fechaFin", fechaFin)
				.getResultList();
		if (!list.isEmpty()) {
			return (List<Ticket>) list;
		}
		return new ArrayList<Ticket>();
	}
	
	@SuppressWarnings("unchecked")
	public List<Ticket> getTicketsSummaryRecepcion(String rucComprobante, String fechaInicio, String fechaFin) 
	{
		Date fechadesde =  OseUtils.strToDateTime(fechaInicio + " 00:00:00");
		Date fechahasta = OseUtils.strToDateTime(fechaFin + " 23:59:59");
		List<?> list = entityManager
				.createQuery("SELECT DISTINCT(t) FROM Ticket t JOIN FETCH t.emisor WHERE t.numRuc=:numRuc AND " +
						     "t.estatus='0' and (t.codDoc='RC' or t.codDoc='RA' or t.codDoc='RR') AND " +
						     "t.fechaRecepcionXml >= :fechaInicio and t.fechaRecepcionXml <= :fechaFin " +
						     "ORDER BY t.id")
				.setParameter("numRuc", rucComprobante)
				.setParameter("fechaInicio", fechadesde)
				.setParameter("fechaFin", fechahasta)
				.getResultList();
		if (!list.isEmpty()) {
			return (List<Ticket>) list;
		}
		return new ArrayList<Ticket>();
		
	}	
	@SuppressWarnings("unchecked")
	public List<Ticket> getTickets(Date fechaInicio, Date fechaFin) 
	{
		List<?> list = entityManager
				.createQuery("SELECT DISTINCT(t) FROM Ticket t JOIN FETCH t.emisor WHERE t.estatus='0' AND " +
						     "t.fechaRecepcionXml >= :fechaInicio and t.fechaRecepcionXml <= :fechaFin " +
						     "ORDER BY t.id")
				.setParameter("fechaInicio", fechaInicio)
				.setParameter("fechaFin", fechaFin)
				.getResultList();
		if (!list.isEmpty()) {
			return (List<Ticket>) list;
		}
		return new ArrayList<Ticket>();
	}	
}
