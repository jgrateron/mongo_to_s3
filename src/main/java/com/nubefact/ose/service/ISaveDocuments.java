package com.nubefact.ose.service;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import com.nubefact.ose.entity.Migrado;
import com.nubefact.ose.entity.Ticket;
import com.nubefact.ose.entity.mongo.MongoCpe;

public interface ISaveDocuments extends Runnable{

	public void setMongoCpe(MongoCpe mongoCpe);
	
	public void setMigrado(Migrado migrado);
	
	public void setTicket(Ticket ticket);
	
	public void setMutex(Semaphore mutex);
	
	public void saveCpe() throws IOException;
	
	public void saveCdr() throws IOException;
	
	public void saveCdrSunat() throws IOException;
	
}
