package com.nubefact.ose.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OseUtils {

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

}
