package com.nubefact.ose.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.nubefact.ose.entity.Ticket;

public interface ITicketDAO {

	Ticket getTicket(String numTicket);

	Ticket getTicket(UUID uuid);

	Ticket getTicket(Long idTicket);

	Ticket getTicket(String rucComprobante, String tipoComprobante, String serieComprobante,
			String numeroComprobante);

	List<Ticket> getTicketsSummaryEmision(String rucComprobante, String fechaInicio, String fechaFin);

	List<Ticket> getTicketsSummaryRecepcion(String rucComprobante, String fechaInicio, String fechaFin);

	List<Ticket> getTicketsPorEnviar(String ruc);

	void update(Ticket ticket);

	void deleteAll(String ruc);
	
	List<Ticket> getTickets(Date fechaInicio, Date fechaFin);	
}
