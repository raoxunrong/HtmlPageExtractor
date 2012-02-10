package catfish.config;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class DIConfig {

	private static Injector injector = Guice.createInjector(ConfigModule
			.getInstance());

	public static Injector getInjector() {
		return injector;
	}
}
