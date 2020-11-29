package it.cambi.dhis2.repository;

import it.cambi.dhis2.model.DataElement;
import it.cambi.dhis2.model.DataElementGroup;
import it.cambi.dhis2.model.DataElementGroupWrap;
import it.cambi.dhis2.model.DataElementWrap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DataElementRepository extends AbstractRepository {

  private final RestTemplate restTemplate;

  private final HttpEntity<String> defaultHttpEntityRequest;

  @Value("${dhis2.url}")
  private String dhis2Url;

  @Value("${dhis2.api.data.elements}")
  private String dhis2ApiDataElements;

  @Value("${dhis2.api.data.elements.group}")
  private String dhis2ApiDataElementGroups;

  public List<DataElement> getDataElements() {

    return Optional.ofNullable(
            exchange(
                    () ->
                        restTemplate.exchange(
                            dhis2Url + dhis2ApiDataElements,
                            HttpMethod.GET,
                            defaultHttpEntityRequest,
                            DataElementWrap.class))
                .getBody())
        .orElse(DataElementWrap.builder().dataElements(Collections.emptyList()).build())
        .getDataElements();
  }

  public List<DataElementGroup> getDataElementGroups() {

    return Optional.ofNullable(
            exchange(
                    () ->
                        restTemplate.exchange(
                            dhis2Url + dhis2ApiDataElementGroups,
                            HttpMethod.GET,
                            defaultHttpEntityRequest,
                            DataElementGroupWrap.class))
                .getBody())
        .orElse(DataElementGroupWrap.builder().dataElementGroups(Collections.emptyList()).build())
        .getDataElementGroups();
  }
}
