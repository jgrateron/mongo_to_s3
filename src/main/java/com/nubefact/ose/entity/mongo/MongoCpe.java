package com.nubefact.ose.entity.mongo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.nubefact.ose.data.ContentFile;
import com.nubefact.ose.entity.Ticket;
import com.nubefact.ose.utils.OseUtils;
import com.nubefact.parametros.EstatusEnvioSunat;

@Document(collection = "cpe")
@SuppressWarnings("unused")
public class MongoCpe {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MongoCpe.class);
	
	@Id
	private String id;
	
	@Indexed(unique = true)
	private Long idTicket;
	
	private String ruc;
	
	@Indexed(unique = true)
	private String nombreDoc;
	
	private String xml;

	private String content;

	private String enconding;
	
	private Date fechaRecepcion;
	
	private Date fechaRespuesta;
	
	@Field("ind_estado_cpe")
	private String estado;
	
	private EstatusEnvioSunat estatusEnvioSunat;

	private MongoCdr mongoCdr;
	
	private MongoCdrSunat mongoCdrSunat;

	public MongoCpe() {

	}

	public void setId(String id)
	{
		this.id = id;
	}
	/**
	 * 
	 */
	public String getCpeZipBase64()
	{
		if (xml.isEmpty()) {
			return content;
		}
		else
		{
			String nombreCpe = this.nombreDoc;
			if(this.nombreDoc.indexOf(".xml") == -1){
				nombreCpe = this.nombreDoc +".xml";
			}			
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
			try 
			{
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Nombre del CPE " + nombreCpe);
				}
				ZipEntry zipEntry = new ZipEntry(nombreCpe);
				zipOutputStream.putNextEntry(zipEntry);
				if (this.enconding == null || this.enconding.length() == 0)
					zipOutputStream.write(this.xml.getBytes());
				else
					zipOutputStream.write(this.xml.getBytes(this.enconding));
				
				zipOutputStream.closeEntry();
				zipOutputStream.close();
				return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
			} catch (Exception e) {
				LOGGER.error("Generando ZIP para Ticket: " + this.idTicket +" Mensaje: " + e.getMessage());
				return null;
			}			
		}
	}
	/**
	 * 
	 */
	public byte[] getCpeCdrZip() 
	{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Generando ZIP para Bajada a Sunat ");
		}
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
		try 
		{
			String nombreCpe = this.nombreDoc;
			if(this.nombreDoc.indexOf(".xml") == -1){
				nombreCpe = this.nombreDoc +".xml";
			}

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Nombre del CPE " + nombreCpe);
			}
			try (InputStream in = getInputStream()){
				ByteArrayOutputStream cpe = OseUtils.InputStreamToOutputStream(in);
				ZipEntry zipEntry = new ZipEntry(nombreCpe);
				zipOutputStream.putNextEntry(zipEntry);
				zipOutputStream.write(cpe.toByteArray());
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Nombre del CDR " + this.mongoCdr.getFilename());
			}
			try (InputStream in = this.mongoCdr.getInputStream()){
				ByteArrayOutputStream cdr = OseUtils.InputStreamToOutputStream(in);
				ZipEntry zipEntry = new ZipEntry(this.mongoCdr.getFilename());
				zipOutputStream.putNextEntry(zipEntry);
				zipOutputStream.write(cdr.toByteArray());					
			}
			zipOutputStream.closeEntry();
			zipOutputStream.close();
		} catch (Exception e) {
			LOGGER.error("Generando ZIP para Ticket: " + this.idTicket +" Mensaje: " + e.getMessage());
		}
		return byteArrayOutputStream.toByteArray();
	}

	public MongoCdr getMongoCdr() {
		return mongoCdr;
	}

	public String getNombreDoc() {
		return nombreDoc+".zip";
	}

	public void setMongoCdrSunat(MongoCdrSunat mongoCdrSunat) {
		this.mongoCdrSunat = mongoCdrSunat;
	}

	public Long getIdTicket() {
		return idTicket;
	}

	public MongoCdrSunat getMongoCdrSunat() {
		return mongoCdrSunat;
	}

	public void setMongoCdr(MongoCdr mongoCdr) {
		this.mongoCdr = mongoCdr;
	}

	public String getEnconding() {
		return enconding;
	}

	public void setEnconding(String enconding) {
		this.enconding = enconding;
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
	
	public InputStream getInputStream() throws IOException
	{
		if (xml.isEmpty()) {
			ContentFile contentFile = new ContentFile();
			contentFile.setContent(content);
			return contentFile.getContentStream();
		}
		else
		{
			if (enconding == null || this.enconding.length() == 0) {
				return OseUtils.strToInputStream(xml);
			}
			else {
				return OseUtils.strToInputStream(xml,enconding);	
			}
		}
	}

	@Override
	public String toString() {
		return "MongoCpe [id=" + id + ", idTicket=" + idTicket + ", ruc=" + ruc + ", nombreDoc=" + nombreDoc
				+ ", enconding=" + enconding + ", fechaRecepcion=" + fechaRecepcion + ", fechaRespuesta="
				+ fechaRespuesta + ", estado=" + estado + ", estatusEnvioSunat=" + estatusEnvioSunat + "]";
	}


	
}
