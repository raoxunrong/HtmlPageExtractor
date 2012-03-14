package catfish.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class DIConfig {

	private static Injector injector = Guice.createInjector(ConfigModule
			.getInstance());

	public static Injector getInjector() {
		return injector;
	}
	
	private static class ConfigModule implements Module {
		
		private static ConfigModule module = new ConfigModule();
		
		public static ConfigModule getInstance(){
			return module;
		}

		@Override
		public void configure(Binder binder) {
			binder.bind(HttpClient.class).to(DefaultHttpClient.class);
		}

	}
}

