package com.nubefact.ose.entity.mongo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoCdr {
   
	private static final Logger LOGGER = LoggerFactory.getLogger(MongoCdr.class);
	
    private String filename;
    private String xml;
	private String content;
	
    private Date fechaGeneracion;
    
    public MongoCdr() {
    	
    }

	public String getFilename() {
		return filename;
	}

	public Date getFechaGeneracion() {
		return fechaGeneracion;
	}

	public void setFechaGeneracion(Date fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public boolean isXmlEmpty()
	{
		return xml.isEmpty();
	}

	public void setContent(String content)
	{
		this.content = content;
		setXml("");
	}
	
	public String getContent() {
		return content;
	}
	
	public String getCdrZipBase64() 
	{
		if (this.xml.isEmpty()) {
			return this.getContent();
		}
		else
		{
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
			try {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Nombre del CDR " + this.getFilename());
				}
				ZipEntry zipEntry = new ZipEntry(this.getFilename());
				zipOutputStream.putNextEntry(zipEntry);
				zipOutputStream.write(this.xml.getBytes());

				zipOutputStream.closeEntry();
				zipOutputStream.close();
				return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
			} catch (Exception e) {
				LOGGER.error("Generando ZIP para Crd: " + this.getFilename() + " Mensaje: " + e.getMessage());
				return null;
			}
		}
	}

	public InputStream getZipInputStream() {		
		byte[] decodedBytes = Base64.getDecoder().decode(getCdrZipBase64());
		return new ByteArrayInputStream(decodedBytes);
	}
}