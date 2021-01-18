package com.nubefact.ose.service;

import java.io.IOException;

import com.nubefact.ose.entity.Ticket;
import com.nubefact.ose.entity.mongo.MongoCdr;
import com.nubefact.ose.entity.mongo.MongoCdrSunat;
import com.nubefact.ose.entity.mongo.MongoCpe;

public interface ISaveDocuments {

	public void saveCpe(MongoCpe mongoCpe, Ticket ticket) throws IOException;
	
	public void saveCdr(MongoCdr mongoCdr, Ticket ticket) throws IOException;
	
	public void saveCdrSunat(MongoCdrSunat mongoCdrSunat, Ticket ticket) throws IOException;
	
}
