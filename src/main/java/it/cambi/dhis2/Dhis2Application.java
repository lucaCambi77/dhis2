package it.cambi.dhis2;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class Dhis2Application {

  @Value("${dhis2.username}")
  private String dhis2Username;

  @Value("${dhis2.password}")
  private String dhis2Password;

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

  @Bean
  public HttpEntity<String> getDefaultHttpEntityRequest() {

    String auth = dhis2Username + ":" + dhis2Password;
    byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.set("Authorization", "Basic " + new String(encodedAuth));

    return new HttpEntity<>(new HttpHeaders(headers));
  }
}
