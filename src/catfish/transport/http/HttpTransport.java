package catfish.transport.http;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

import catfish.constants.HttpConstants;
import catfish.model.FetchedDocument;
import catfish.transport.common.Transport;
import catfish.transport.exception.TransportException;


public class HttpTransport implements Transport {
	
	private HttpState initialState;
	
	private HttpClient httpclient;

	private static final int MAX_DOCUMENT_LENGTH = 1024 * 1024;

	@Override
	public void clear() {
		httpclient = null;
		initialState = null;
	}

	@Override
	public FetchedDocument fetch(String url) throws TransportException {
		GetMethod httpget = null;
		FetchedDocument doc = null;
		try {
			httpget = new GetMethod(url);
			httpget.setFollowRedirects(true);
			int result = httpclient.executeMethod(httpget);
			if (result != HttpConstants.REQUEST_OK) {
				String errorMessage = "HTTP Response code: " + result
						+ ", status text: " + httpget.getStatusText()
						+ " for url: '" + url + "'.";
				throw new TransportException(errorMessage);
			} else {
				doc = createDocument(url, httpget);
			}
		} catch (IOException e) {
			throw new TransportException("Failed to fetch url: '" + url
					+ "': ", e);
		} finally {
			if (httpget != null) {
				httpget.releaseConnection();
			}
		}

		return doc;
	}

	private FetchedDocument createDocument(String url, GetMethod httpget) throws IOException, HTTPTransportException {
		FetchedDocument doc = new FetchedDocument();

		/* IOException will be thrown for documents that exceed max length */
		byte[] data = httpget.getResponseBody(MAX_DOCUMENT_LENGTH);

		/*
		 * Check if server sent content in compressed form and uncompress the
		 * content if necessary.
		 */
		Header contentEncodingHeader = httpget
				.getResponseHeader(HttpConstants.RESPONSE_HEADER_ENCODING);
		if (contentEncodingHeader != null) {
				data = HttpUtils.decodeContent(
						contentEncodingHeader.getValue(), data);
		}

		/* 'Content-Type' HTTP header value */
		String contentTypeHeaderValue = null;
		Header header = httpget.getResponseHeader(HttpConstants.RESPONSE_HEADER_TYPE);
		if (header != null) {
			contentTypeHeaderValue = header.getValue();
		}

		/*
		 * Determine MIME type of the document.
		 * 
		 * It is easy if we have Content-Type http header. In cases when this
		 * header is missing or for protocols that don't pass metadata about the
		 * documents (ftp://, file://) we would have to resort to url and/or
		 * content analysis to determine MIME type.
		 */
		String contentType = HttpUtils.getContentType(contentTypeHeaderValue,
				url, data);
		/*
		 * Determine Character encoding used in the document. In some cases it
		 * may be specified in the http header, in html file itself or we have
		 * to perform content analysis to choose the encoding.
		 */
		String contentCharset = HttpUtils.getCharset(contentTypeHeaderValue,
				contentType, data);

		doc.setContentType(contentType);
		doc.setDocumentURL(url);
		doc.setContentCharset(contentCharset);
		doc.setDocumentContent(data);
		doc.setDocumentMetadata(new HashMap<String, String>());
		return doc;
	}

	@Override
	public void init() {
		if(initialState == null){
			initialState = new HttpState();
		}
		if(httpclient == null){
			httpclient = new HttpClient();
		}
		httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(
				30000);
		httpclient.getHttpConnectionManager().getParams().setSoTimeout(30000);
		httpclient.setState(initialState);
		httpclient.getParams().setCookiePolicy(
				CookiePolicy.BROWSER_COMPATIBILITY);
		httpclient.getParams().setParameter(
				HttpClientParams.ALLOW_CIRCULAR_REDIRECTS, Boolean.TRUE);
		// Set default number of connections per host to 1
		httpclient.getHttpConnectionManager().getParams()
				.setMaxConnectionsPerHost(
						HostConfiguration.ANY_HOST_CONFIGURATION, 1);
		// Set max for total number of connections
		httpclient.getHttpConnectionManager().getParams()
				.setMaxTotalConnections(10);
	}

	@Override
	public boolean pauseRequired() {
		return true;
	}


}
