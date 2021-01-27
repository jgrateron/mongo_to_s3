package com.nubefact.ose.service.impl;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nubefact.ose.entity.Migrado;
import com.nubefact.ose.entity.Ticket;
import com.nubefact.ose.entity.mongo.MongoCpe;
import com.nubefact.ose.service.ISaveDocuments;

@Component("SaveDocumentToNull")
@Scope("prototype")
public class SaveDocumentToNull implements ISaveDocuments {

	private Semaphore mutex;
	
	@Override
	public void run() 
	{
		mutex.release();
	}

	@Override
	public void setMongoCpe(MongoCpe mongoCpe) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTicket(Ticket ticket) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMutex(Semaphore mutex) {
		this.mutex = mutex;
	}

	@Override
	public void saveCpe() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveCdr() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveCdrSunat() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMigrado(Migrado migrado) {
		// TODO Auto-generated method stub
		
	}

}
