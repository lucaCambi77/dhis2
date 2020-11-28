package it.cambi.dhis2.repository;

import it.cambi.dhis2.model.DataElement;
import it.cambi.dhis2.model.DataElementGroup;
import it.cambi.dhis2.model.DataElementGroupWrap;
import it.cambi.dhis2.model.DataElementWrap;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DataElementRepository extends AbstractRepository {

  private final RestTemplate restTemplate;

  @Value("${dhis2.url}")
  private String dhis2Url;

  @Value("${dhis2.api.data.elements}")
  private String dhis2ApiDataElements;

  @Value("${dhis2.api.data.elements.group}")
  private String dhis2ApiDataElementGroups;

  @Value("${dhis2.username}")
  private String dhis2Username;

  @Value("${dhis2.password}")
  private String dhis2Password;

  public List<DataElement> getDataElements() {

    return Optional.ofNullable(
            exchange(
                    () ->
                        restTemplate.exchange(
                            dhis2Url + dhis2ApiDataElements,
                            HttpMethod.GET,
                            getHttpEntityRequest(dhis2Username, dhis2Password),
                            DataElementWrap.class))
                .getBody())
        .orElse(DataElementWrap.builder().build())
        .getDataElements();
  }

  public List<DataElementGroup> getDataElementGroups() {

    return Optional.ofNullable(
            exchange(
                    () ->
                        restTemplate.exchange(
                            dhis2Url + dhis2ApiDataElementGroups,
                            HttpMethod.GET,
                            getHttpEntityRequest(dhis2Username, dhis2Password),
                            DataElementGroupWrap.class))
                .getBody())
        .orElse(DataElementGroupWrap.builder().build())
        .getDataElementGroups();
  }

  private HttpEntity<String> getHttpEntityRequest(String username, String password) {
    HttpHeaders httpHeaders =
        new HttpHeaders() {
          {
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
          }
        };

    return new HttpEntity<>(httpHeaders);
  }
}
