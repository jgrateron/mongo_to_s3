package com.nubefact.ose.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.bson.internal.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.nubefact.ose.entity.Ticket;
import com.nubefact.ose.entity.mongo.MongoCdr;
import com.nubefact.ose.entity.mongo.MongoCdrSunat;
import com.nubefact.ose.entity.mongo.MongoCpe;
import com.nubefact.ose.service.ISaveDocuments;

@Component("SaveDocumentToDisk")
@Scope("prototype")
public class SaveDocumentToDisk implements ISaveDocuments {

	private static final Logger logger = LoggerFactory.getLogger(SaveDocumentToDisk.class);	
	private Ticket ticket;
	private MongoCpe mongoCpe; 

	@Override
	public void run() 
	{
		try 
		{
			logger.debug("save " + ticket.getNombreDoc());
			saveCpe();
			saveCdr();
			saveCdrSunat();
			Thread.sleep(0);
		} 
		catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void setMongoCpe(MongoCpe mongoCpe) {
		this.mongoCpe = mongoCpe;
	}

	@Override
	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	@Override
	public void saveCpe() throws IOException {
		String path = "/tmp/cpe/" + ticket.getNumRuc() + "/";
		File file = new File(path);
		file.mkdir();
		String zipbase64 = mongoCpe.getCpeZipBase64();
		String fileName = path + ticket.getNombreDoc() + ".zip";
		saveFile(fileName,zipbase64);
	}

	@Override
	public void saveCdr() throws IOException {
		MongoCdr mongoCdr = mongoCpe.getMongoCdr();
		String path = "/tmp/cdr/" + ticket.getNumRuc() + "/";
		File file = new File(path);
		file.mkdir();
		String zipbase64 = mongoCdr.getCdrZipBase64();
		String fileName = path + "R-" + ticket.getNombreDoc() + ".zip";
		saveFile(fileName,zipbase64);
	}

	@Override
	public void saveCdrSunat() throws IOException {
		MongoCdrSunat mongoCdrSunat = mongoCpe.getMongoCdrSunat();
		if (mongoCdrSunat != null)
		{
			String zipbase64 = mongoCdrSunat.getCdrSunatZipBase64();
			String path = "/tmp/sunat/" + ticket.getNumRuc() + "/";
			File file = new File(path);
			file.mkdir();
			String fileName = path + "/R-" + ticket.getNombreDoc() + ".zip";
			saveFile(fileName,zipbase64);				
		}
	}

	public void saveFile(String fileName, String zipbase64) throws IOException {
		FileOutputStream outputStream = new FileOutputStream(fileName);			
		byte[] strToBytes = Base64.decode(zipbase64);
		outputStream.write(strToBytes);
		outputStream.close();
	}

}
