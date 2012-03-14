package catfish.transport.http;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.junit.Before;
import org.junit.Test;

import catfish.model.FetchedDocument;
import catfish.transport.exception.TransportException;

public class HttpTransportTest {

	private HttpTransport httpTransport;
	private HttpClient httpClient;
	private ClientConnectionManager connectionManager;

	@SuppressWarnings("unchecked")
	@Before
	public void init() throws ClientProtocolException, IOException {

		httpTransport = new HttpTransport();
		httpClient = mock(HttpClient.class);
		connectionManager = mock(ClientConnectionManager.class);
		when(httpClient.getConnectionManager()).thenReturn(connectionManager);
		when(httpClient.execute((HttpUriRequest)any(), (ResponseHandler<FetchedDocument>)any())).thenReturn(new FetchedDocument());
		httpTransport.setHttpclient(httpClient);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void should_httpclient_execute_with_specified_responseHandler_when_result_is_FetchedDocument()
			throws TransportException, ClientProtocolException, IOException {
		String url = "http://www.baidu.com";
		FetchedDocument document = httpTransport.fetch(url);
		
		verify(httpClient).execute((HttpUriRequest)any(), (ResponseHandler<FetchedDocument>)any());
		verify(connectionManager).shutdown();
		assertEquals(url, document.getDocumentURL());
	}

}
