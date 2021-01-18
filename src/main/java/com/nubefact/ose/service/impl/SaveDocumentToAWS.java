package com.nubefact.ose.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.nubefact.ose.entity.Ticket;
import com.nubefact.ose.entity.mongo.MongoCdrSunat;
import com.nubefact.ose.entity.mongo.MongoCpe;
import com.nubefact.ose.service.ISaveDocuments;

@Component("SaveDocumentToAWS")
@Scope("prototype")
public class SaveDocumentToAWS implements ISaveDocuments {

	private static final Logger logger = LoggerFactory.getLogger(SaveDocumentToAWS.class);	
	private Ticket ticket;
	private MongoCpe mongoCpe; 
	private Semaphore mutex;
	
	@Autowired
	private AmazonS3 amazonS3;

	@Value("${ose.bucket_name}")
	private String bucket_name;
	
	private static String PATHCPE = "cpe/";
	private static String PATHCDR = "cdr/";
	private static String PATHSUNAT = "sunat/";
	private static String PREFIXCDR = "R-";
	private static String CONTENTTYPE= "application/zip" ;
	private static String EXTZIP = ".zip";
	
	@Override
	public void run() 
	{
		try 
		{
			logger.debug("save " + ticket.getNombreDoc());			
			saveCpe();
			saveCdr();
			saveCdrSunat();			
		} 
		catch (Exception e) {
			logger.error(ticket.getNombreDoc() + " " + e.getMessage());
		}
		mutex.release();
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
	public void saveCpe() throws IOException 
	{
		String pathRuc = mongoCpe.getRuc() + "/";
		try(InputStream isXml = mongoCpe.getZipInputStream()){			
			savefile(isXml,PATHCPE + pathRuc + mongoCpe.getNombreDoc());
		}		
	}

	@Override
	public void saveCdr() throws IOException 
	{	
		String pathRuc = mongoCpe.getRuc() + "/";
    	try (InputStream isCdr = mongoCpe.getMongoCdr().getZipInputStream()){
    		savefile(isCdr,PATHCDR + pathRuc + PREFIXCDR + mongoCpe.getNombreDoc());
    	}		
	}

	@Override
	public void saveCdrSunat() throws IOException 
	{
		MongoCdrSunat mongoCdrSunat = mongoCpe.getMongoCdrSunat();
		if (mongoCdrSunat != null)
		{				
			String nameCdr = mongoCpe.getMongoCdrSunat().getCdrFileName();
			nameCdr = nameCdr.replaceAll(".xml", EXTZIP);
			String pathRuc = mongoCpe.getRuc() + "/";
			try(InputStream isCdr = mongoCdrSunat.getCdrZipInputStream()){
				savefile(isCdr, PATHSUNAT + pathRuc + nameCdr);
			}
		}		
	}

	@Override
	public void setMutex(Semaphore mutex) {
		this.mutex = mutex;
	}
	
	private void savefile(InputStream is, String key) throws IOException
	{
    	ObjectMetadata metadata = new ObjectMetadata();
    	metadata.setContentType(CONTENTTYPE);
    	metadata.setContentLength(is.available());
    	amazonS3.putObject(bucket_name,key,is,metadata);		
	}	
}
