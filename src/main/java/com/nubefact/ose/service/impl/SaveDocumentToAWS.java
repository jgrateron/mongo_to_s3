package com.nubefact.ose.service.impl;

import org.springframework.stereotype.Service;

import com.nubefact.ose.entity.Ticket;
import com.nubefact.ose.entity.mongo.MongoCdr;
import com.nubefact.ose.entity.mongo.MongoCdrSunat;
import com.nubefact.ose.entity.mongo.MongoCpe;
import com.nubefact.ose.service.ISaveDocuments;

@Service("SaveDocumentToAWS")
public class SaveDocumentToAWS implements ISaveDocuments {

	@Override
	public void saveCpe(MongoCpe mongoCpe, Ticket ticket) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveCdr(MongoCdr mongoCdr, Ticket ticket) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveCdrSunat(MongoCdrSunat mongoCdrSunat, Ticket ticket) {
		// TODO Auto-generated method stub

	}

}
