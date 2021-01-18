package com.nubefact.ose;

import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import com.nubefact.ose.service.impl.SaveDocuments;

@SpringBootApplication
@EntityScan(value = {"com.nubefact.sunat.entity","com.nubefact.ose.entity"})
public class MongoToS3Application implements CommandLineRunner{

	@Autowired
	private SaveDocuments saveDocuments;

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
		SpringApplication.run(MongoToS3Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception 
	{
		saveDocuments.run();
		
	}


}
