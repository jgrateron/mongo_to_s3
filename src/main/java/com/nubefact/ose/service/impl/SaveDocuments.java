package com.nubefact.ose.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.nubefact.ose.dao.IMongoCpeDAO;
import com.nubefact.ose.dao.ITicketDAO;
import com.nubefact.ose.entity.Ticket;
import com.nubefact.ose.entity.mongo.MongoCpe;
import com.nubefact.ose.service.ISaveDocuments;
import com.nubefact.ose.utils.OseUtils;

@Service
public class SaveDocuments {

	private static final Logger logger = LoggerFactory.getLogger(SaveDocuments.class);
	
	@Autowired
	private IMongoCpeDAO mongoCpeDAO;
	
	@Autowired
	private ITicketDAO ticketDAO;
	
    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private TaskExecutor threadPoolTaskExecutor;    
    
    private Semaphore mutex = new Semaphore(0);
    
	public void run()
	{
		Date fechaInicio = OseUtils.strToDateTime("2021-01-18 00:00:00");
		Date fechaFin = OseUtils.strToDateTime("2021-01-18 23:59:59");
		long startTime = System.nanoTime();
		
		List<Ticket> tickets = ticketDAO.getTickets(fechaInicio, fechaFin);
		
		for (Ticket ticket : tickets)
		{
			MongoCpe mongoCpe = mongoCpeDAO.getByIdTicket(ticket.getId());
			ISaveDocuments saveDocuments = applicationContext.getBean(SaveDocumentToAWS.class);
			saveDocuments.setMongoCpe(mongoCpe);
			saveDocuments.setTicket(ticket);
			saveDocuments.setMutex(mutex);
			threadPoolTaskExecutor.execute(saveDocuments);
		}
		logger.debug("Total: " + tickets.size());
		for (int i = 0; i < tickets.size(); i++)
		{
			try 
			{
				mutex.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		OseUtils.tiempoDuracion(startTime,"");
		((ConfigurableApplicationContext)applicationContext).close();
	}
}
