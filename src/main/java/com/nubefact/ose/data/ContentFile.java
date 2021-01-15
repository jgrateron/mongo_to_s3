package com.nubefact.ose.data;

import java.io.IOException;
import java.io.InputStream;

import com.nubefact.ose.utils.OseUtils;

public class ContentFile {

	private String content;
	private InputStream contentStream = null;
	
	public String getContent() {
		return content;
	}

	public InputStream getContentStream() {
		return contentStream;
	}

	public void setContent(String content) throws IOException {
		this.content = content;
		this.contentStream = OseUtils.getDocumentBase64(content);
	}
	
	public void close() throws IOException
	{
		if (contentStream != null) {
			contentStream.close();
		}
	}
}
