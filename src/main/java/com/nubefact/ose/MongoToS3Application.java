package com.nubefact.ose;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import com.nubefact.ose.dao.IMongoCpeDAO;
import com.nubefact.ose.dao.ITicketDAO;
import com.nubefact.ose.entity.Ticket;
import com.nubefact.ose.entity.mongo.MongoCpe;
import com.nubefact.ose.service.ISaveDocuments;
import com.nubefact.ose.utils.OseUtils;

@SpringBootApplication
@EntityScan(value = {"com.nubefact.sunat.entity","com.nubefact.ose.entity"})
public class MongoToS3Application implements CommandLineRunner{

	private static final Logger logger = LoggerFactory.getLogger(MongoToS3Application.class);
	
	@Autowired
	private IMongoCpeDAO mongoCpeDAO;
	
	@Autowired
	private ITicketDAO ticketDAO;
	
	@Qualifier("SaveDocumentToDisk")
	@Autowired
	private ISaveDocuments saveDocuments;
	
	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
		SpringApplication.run(MongoToS3Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	
		Date fechaInicio = OseUtils.strToDateTime("2021-01-18 00:00:00");
		Date fechaFin = OseUtils.strToDateTime("2021-01-18 23:59:59");
		
		List<Ticket> tickets = ticketDAO.getTickets(fechaInicio, fechaFin);
		System.out.println(tickets.size());
		for (Ticket ticket : tickets)
		{
			logger.debug("save " + ticket.getNombreDoc());
			MongoCpe mongoCpe = mongoCpeDAO.getByIdTicket(ticket.getId());
			saveDocuments.saveCpe(mongoCpe, ticket);
			saveDocuments.saveCdr(mongoCpe.getMongoCdr(), ticket);
			saveDocuments.saveCdrSunat(mongoCpe.getMongoCdrSunat(), ticket);
		}
	}	
}
