package catfish.config;

import static org.junit.Assert.*;

import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import catfish.transport.http.HttpTransport;

public class DIConfigTest {

	@Test
	public void should_inject_parameters_to_HttpTransport_instance_when_fetch_from_DIConfig(){
		HttpTransport instance = DIConfig.getInjector().getInstance(HttpTransport.class);
		
		assertNotNull(instance);
		assertTrue(instance.getHttpclient() instanceof DefaultHttpClient);
	}
}
