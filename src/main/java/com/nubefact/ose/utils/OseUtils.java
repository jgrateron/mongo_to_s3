package com.nubefact.ose.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	
	public static Date incDate(Date dt)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.add(Calendar.DATE, 1);
		return c.getTime();
	}
	
    public static void systemInformation()
    {
		long heap = Runtime.getRuntime().totalMemory();    
		// y aca el max del heap disponible en esta jvm
		long heapMax = Runtime.getRuntime().maxMemory();
		// y aca finalmente lo que esta libre
		long heapFree = Runtime.getRuntime().freeMemory();
		if (logger.isInfoEnabled())
		{
			logger.info("Heap " + (heap /1024)/1024 + " MB");
			logger.info("Heapmax " + (heapMax / 1024)/1024 + " MB");
			logger.info("heapFree " + (heapFree /1024)/1024 + " MB");			
		}
    }	
}
