package catfish.config;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;

import com.google.inject.Binder;
import com.google.inject.Module;

public class ConfigModule implements Module {
	
	private static ConfigModule module = new ConfigModule();
	
	public static ConfigModule getInstance(){
		return module;
	}

	@Override
	public void configure(Binder binder) {
//		binder.bind(HttpState.class).to(HttpState.class);
//		binder.bind(HttpClient.class).to(HttpClient.class);
	}

}
