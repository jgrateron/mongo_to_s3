package com.nubefact.ose.service;

import java.io.IOException;

import com.nubefact.ose.entity.Ticket;
import com.nubefact.ose.entity.mongo.MongoCpe;

public interface ISaveDocuments extends Runnable{

	public void setMongoCpe(MongoCpe mongoCpe);
	
	public void setTicket(Ticket ticket);
	
	public void saveCpe() throws IOException;
	
	public void saveCdr() throws IOException;
	
	public void saveCdrSunat() throws IOException;
	
}
