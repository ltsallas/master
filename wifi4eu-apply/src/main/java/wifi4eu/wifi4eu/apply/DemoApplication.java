package wifi4eu.wifi4eu.apply;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.core.env.Environment;

import java.io.IOException;


@SpringBootApplication
public class DemoApplication extends SpringBootServletInitializer implements CommandLineRunner {

	@Autowired
	private Environment environment;



	@Override
	public void run(String... args) throws Exception {

		Config.init(environment);

		new LocalDB();
	}

	public static void main(String[] args) throws IOException {

		SpringApplication.run(DemoApplication.class, args);
	}


}