package com.nubefact.ose.service.impl;

import java.io.IOException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.nubefact.ose.entity.Ticket;
import com.nubefact.ose.entity.mongo.MongoCpe;
import com.nubefact.ose.service.ISaveDocuments;

@Component("SaveDocumentToAWS")
@Scope("prototype")
public class SaveDocumentToAWS implements ISaveDocuments {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
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
}
