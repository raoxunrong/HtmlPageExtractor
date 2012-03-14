package catfish.transport.http;

import java.io.IOException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import catfish.model.FetchedDocument;
import catfish.transport.common.Transport;
import catfish.transport.exception.TransportException;
import catfish.transport.http.handler.FetchedDocumentResponseHandler;

import com.google.inject.Inject;


public class HttpTransport implements Transport {
	
	@Inject
	private HttpClient httpclient;
	
	@Override
	public void clear() {
		httpclient = null;
	}

	@Override
	public FetchedDocument fetch(String url) throws TransportException {
		HttpGet httpget = null;
		FetchedDocument doc = null;
		try {
			httpget = new HttpGet(url);
			ResponseHandler<FetchedDocument> responseHandler = new FetchedDocumentResponseHandler();
			doc = httpclient.execute(httpget, responseHandler);
			doc.setDocumentURL(url);
		} catch (IOException e) {
			throw new TransportException("Failed to fetch url: '" + url
					+ "': ", e);
		} finally {
			if(httpclient != null){
				httpclient.getConnectionManager().shutdown();
			}
		}

		return doc;
	}

	@Override
	public void init() {
		if(httpclient == null){
			httpclient = new DefaultHttpClient();
		}
	}

	@Override
	public boolean pauseRequired() {
		return true;
	}

	public void setHttpclient(HttpClient httpclient) {
		this.httpclient = httpclient;
	}

	public HttpClient getHttpclient() {
		return httpclient;
	}

}
