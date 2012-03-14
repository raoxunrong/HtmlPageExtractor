/**
 * 
 */
package catfish.utils;
import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;

import train.TrainConstants.PageType;
import catfish.config.DIConfig;
import catfish.transport.exception.TransportException;
import catfish.transport.http.HttpTransport;


public class PageUtilsTest {

	private HttpTransport httpTransport;

	@Before
	public void init(){
		httpTransport = DIConfig.getInjector().getInstance(HttpTransport.class);
	}
	
	@Test
	public void should_return_notsubject_string_when_url_type_is_notsubject() throws TransportException, UnsupportedEncodingException{
		
		PageType pageType = getPageType("http://tech.163.com/");
		assertEquals(PageType.Hub, pageType);
	}

	@Test
	public void should_return_subject_string_when_url_type_is_subject() throws TransportException, UnsupportedEncodingException{
		PageType pageType = getPageType("http://news.163.com/12/0213/16/7Q5HH5AD00014JB5.html");
		assertEquals(PageType.Subject, pageType);
	}
	
	private PageType getPageType(String url) throws TransportException, UnsupportedEncodingException {
		PageType pageType = PageUtils.getPageType(httpTransport.fetch(url));
		return pageType;
	}
}
