package com.nubefact.ose.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.nubefact.ose.dao.IMigradoDAO;
import com.nubefact.ose.dao.IMongoCpeDAO;
import com.nubefact.ose.entity.Migrado;
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
	private IMigradoDAO migradoS3DAO;
	
    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private TaskExecutor threadPoolTaskExecutor;    
    
	@Value("${ose.fecha_ini}")
	private String fecha_ini;
	
	@Value("${ose.fecha_fin}")
	private String fecha_fin;

	@Value("${ose.save_to}")
	private String save_to;
	
	@Value("${ose.min_thread}")
	private Integer minThread;

	@Value("${ose.min_thread}")
	private Integer maxThread;
	
    private Semaphore mutex = new Semaphore(0);
    
	public void run()
	{
		Date fechaIni = OseUtils.strToDateTime(fecha_ini + " 00:00:00");
		Date fechaFin = OseUtils.strToDateTime(fecha_fin + " 23:59:59");
		long startTime = System.nanoTime();
		
		Date start = fechaIni;
		while (start.before(fechaFin)) 
		{
			Date end = OseUtils.incDate(start);
			List<Migrado> registros = migradoS3DAO.getRegistros(start, end);
			logger.info(start + " Total: " + registros.size());
			int cuantos = 0;
			int total = 0;
			for (Migrado migrado : registros)
			{
				if (migrado.isCpe() && migrado.isCdr_ose() && migrado.isCdr_sunat()) {
					//si está completamente migrado, pasar al siguiente 
					continue;
				}
				Ticket ticket = migrado.getTicket();
				MongoCpe mongoCpe = mongoCpeDAO.getByIdTicket(ticket.getId());
				ISaveDocuments saveDocuments = null;
				if ("AWS".equals(save_to)) {
					saveDocuments = applicationContext.getBean(SaveDocumentToAWS.class); 
				}
				else
				if ("DISK".equals(save_to)) {
					saveDocuments = applicationContext.getBean(SaveDocumentToDisk.class);
				}
				else
				if ("NULL".equals(save_to)) {
					saveDocuments = applicationContext.getBean(SaveDocumentToNull.class);
				}
				cuantos++;	
				total++;
				saveDocuments.setMigrado(migrado);
				saveDocuments.setMongoCpe(mongoCpe);
				saveDocuments.setTicket(ticket);
				saveDocuments.setMutex(mutex);
				threadPoolTaskExecutor.execute(saveDocuments);
				if (cuantos == maxThread) 
				{
					for (int i = 0; i < (maxThread - minThread); i++)
					{
						try 
						{
							mutex.acquire();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						cuantos --;	
					}
				}
				if (total % 10000 == 0) {
					logger.info("guardando " + total + " documentos");
				}										
			}
			for (int i = 0; i < cuantos; i++)
			{
				try {
					mutex.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.info(start + " guardados " + total + " documentos");
			start = end;
		}
		OseUtils.tiempoDuracion(startTime,"");
		((ConfigurableApplicationContext)applicationContext).close();
	}
}
