package com.nubefact.ose.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.bson.internal.Base64;
import org.springframework.stereotype.Service;

import com.nubefact.ose.entity.Ticket;
import com.nubefact.ose.entity.mongo.MongoCdr;
import com.nubefact.ose.entity.mongo.MongoCdrSunat;
import com.nubefact.ose.entity.mongo.MongoCpe;
import com.nubefact.ose.service.ISaveDocuments;

@Service("SaveDocumentToDisk")
public class SaveDocumentToDisk implements ISaveDocuments {

	@Override
	public void saveCpe(MongoCpe mongoCpe, Ticket ticket) throws IOException {
		String path = "/tmp/cpe/" + ticket.getNumRuc() + "/";
		File file = new File(path);
		file.mkdir();
		String zipbase64 = mongoCpe.getCpeZipBase64();
		String fileName = path + ticket.getNombreDoc() + ".zip";
		saveFile(fileName,zipbase64);
	}

	@Override
	public void saveCdr(MongoCdr mongoCdr, Ticket ticket) throws IOException 
	{
		String path = "/tmp/cdr/" + ticket.getNumRuc() + "/";
		File file = new File(path);
		file.mkdir();
		String zipbase64 = mongoCdr.getCdrZipBase64();
		String fileName = path + "R-" + ticket.getNombreDoc() + ".zip";
		saveFile(fileName,zipbase64);
	}

	@Override
	public void saveCdrSunat(MongoCdrSunat mongoCdrSunat, Ticket ticket) throws IOException {
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
