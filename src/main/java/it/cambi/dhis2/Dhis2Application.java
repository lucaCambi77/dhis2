package it.cambi.dhis2;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Dhis2Application {

	public static void main(String[] args) {
		SpringApplication.run(Dhis2Application.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {

		return new RestTemplate();
	}

	@Bean
	public ObjectMapper getObjectMapper() {

		return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
}
