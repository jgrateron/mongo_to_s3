package com.nubefact.ose.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OseUtils {

	private static final Logger logger = LoggerFactory.getLogger(OseUtils.class);
	
	public static Date strToDateTime(String fechaHora) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try 
		{
			return sd.parse(fechaHora);
		} 
		catch (ParseException e) {
			throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	public static void tiempoDuracion(long startTime, String mensaje)
	{
		if (logger.isDebugEnabled()) {
			long endTime = System.nanoTime();
			long duration = endTime - startTime;  //divide by 1000000 to get milliseconds.			
			logger.debug("Tiempo de duracion " + mensaje + ": " + duration /1000000);
		}
	}
}
