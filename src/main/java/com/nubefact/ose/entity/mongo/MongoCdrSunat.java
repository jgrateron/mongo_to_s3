package com.nubefact.ose.entity.mongo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoCdrSunat{

	private static final Logger LOGGER = LoggerFactory.getLogger(MongoCdrSunat.class);
	
	private String cdrFileName;
	private String cdrContent;

	public MongoCdrSunat() {
		
	}
	
	public MongoCdrSunat(byte[] zipDataArray) throws IOException 
	{
		this.cdrFileName = "";		
		this.cdrContent ="";		
		try 
		{
			ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zipDataArray));
			ZipEntry zipEntry;
			try 
			{
				while ((zipEntry = zipInputStream.getNextEntry()) != null) 
				{
					if (zipEntry.getName().equals("dummy/") || !zipEntry.getName().endsWith(".xml")) {
						continue;
		            }
					break;
				}
	        	this.cdrFileName = zipEntry.getName();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Nombre del CDR " + this.getCdrFileName());
				}
	        	this.cdrContent = Base64.getEncoder().encodeToString(zipDataArray);
			} 
			finally {
				zipInputStream.close();
			}
        }
		catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}
	
	public String getCdrFileName() {
		return cdrFileName;
	}

	public String getCdrContent() {
		return cdrContent;
	}

	public void setCdrContent(String cdrContent) {
		this.cdrContent = cdrContent;
	}
	
	public boolean isBase64Content()
	{
		String regex = "^[A-Za-z0-9+/=]{2,}$";
		return cdrContent.matches(regex);
	}
	
	public String getCdrSunatZipBase64() 
	{
		if (isBase64Content()) 
		{
			return this.getCdrContent();
		}
		else
		{
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
			try {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Nombre del CDR " + this.getCdrFileName());
				}
				ZipEntry zipEntry = new ZipEntry(this.getCdrFileName());
				zipOutputStream.putNextEntry(zipEntry);
				zipOutputStream.write(this.cdrContent.getBytes());

				zipOutputStream.closeEntry();
				zipOutputStream.close();
				return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
			} 
			catch (Exception e) {
				LOGGER.error("Generando ZIP para CdrSunat: " + this.cdrFileName +" Mensaje: " + e.getMessage());
				return null;
			}			
		}
	}
}