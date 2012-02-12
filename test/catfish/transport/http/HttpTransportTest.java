package catfish.transport.http;

import junit.framework.Assert;

import org.junit.Test;

import catfish.config.DIConfig;
import catfish.model.FetchedDocument;
import catfish.transport.exception.TransportException;

public class HttpTransportTest {

	@Test
	public void should_xxx_when_xxx() throws TransportException{
		HttpTransport httpTransport = DIConfig.getInjector().getInstance(HttpTransport.class);
//		httpTransport.init();
//		FetchedDocument document = httpTransport.fetch("http://www.baidu.com");
		FetchedDocument document = httpTransport.fetch("http://news.sohu.com/20120208/n334103957.shtml");
		
//		Assert.assertEquals("GBK", document.getContentCharset());
		System.out.println("Charset is " + document.getContentCharset());
		System.out.println("Content length is " + document.getContentLength());
	}
}
