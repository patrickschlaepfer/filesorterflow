package com.schlaepfer.esb.mule;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.transformer.AbstractMessageTransformer;

public class FileRecognizer extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		byte[] data = (byte[]) message.getPayload();
		
		InputStream is = new BufferedInputStream(new ByteArrayInputStream(data));
		String mimeType = null;
		try {
			mimeType = URLConnection.guessContentTypeFromStream(is);
			is.close();
		} catch (IOException e) {
			if(logger.isErrorEnabled()) {
				logger.error("Couldn't determine mimeType");
			}
		}
		if(logger.isInfoEnabled()) {
			logger.info("mimeType is "+mimeType);
		}
		message.setProperty("mimeType", mimeType, PropertyScope.OUTBOUND);;
		
		return message;
	}

}
