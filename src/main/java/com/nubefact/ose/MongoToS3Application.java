package com.nubefact.ose;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.bson.internal.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import com.nubefact.ose.dao.IMongoCpeDAO;
import com.nubefact.ose.dao.ITicketDAO;
import com.nubefact.ose.entity.Ticket;
import com.nubefact.ose.entity.mongo.MongoCdr;
import com.nubefact.ose.entity.mongo.MongoCdrSunat;
import com.nubefact.ose.entity.mongo.MongoCpe;
import com.nubefact.ose.utils.OseUtils;

@SpringBootApplication
@EntityScan(value = {"com.nubefact.sunat.entity","com.nubefact.ose.entity"})
public class MongoToS3Application implements CommandLineRunner{

	@Autowired
	private IMongoCpeDAO mongoCpeDAO;
	
	@Autowired
	private ITicketDAO ticketDAO;
	
	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
		SpringApplication.run(MongoToS3Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	
		Date fechaInicio = OseUtils.strToDateTime("2021-01-15 00:00:00");
		Date fechaFin = OseUtils.strToDateTime("2021-01-15 23:59:59");
		
		List<Ticket> tickets = ticketDAO.getTickets(fechaInicio, fechaFin);
		System.out.println(tickets.size());
		for (Ticket ticket : tickets)
		{
			MongoCpe mongoCpe = mongoCpeDAO.getByIdTicket(ticket.getId());
			String path = "/tmp/cpe/" + ticket.getNumRuc() + "/";
			File file = new File(path);
			file.mkdir();
			String zipbase64 = mongoCpe.getCpeZipBase64();
			String fileName = path + ticket.getNombreDoc() + ".zip";
			saveFile(fileName,zipbase64);
			
			MongoCdr mongoCdr = mongoCpe.getMongoCdr();
			path = "/tmp/cdr/" + ticket.getNumRuc() + "/";
			file = new File(path);
			file.mkdir();
			zipbase64 = mongoCdr.getCdrZipBase64();
			fileName = path + "R-" + ticket.getNombreDoc() + ".zip";
			saveFile(fileName,zipbase64);
			
			MongoCdrSunat mongoCdrSunat = mongoCpe.getMongoCdrSunat();
			if (mongoCdrSunat != null)
			{
				zipbase64 = mongoCdrSunat.getCdrSunatZipBase64();
				path = "/tmp/sunat/" + ticket.getNumRuc() + "/";
				file = new File(path);
				file.mkdir();
				fileName = path + "/R-" + ticket.getNombreDoc() + ".zip";
				saveFile(fileName,zipbase64);				
			}
		}
	}

	public void saveFile(String fileName, String zipbase64) throws IOException
	{
		FileOutputStream outputStream = new FileOutputStream(fileName);			
		byte[] strToBytes = Base64.decode(zipbase64);
		outputStream.write(strToBytes);
		outputStream.close();		
	}
}
