package it.cambi.dhis2.repository;

import it.cambi.dhis2.AbstractTest;
import it.cambi.dhis2.Dhis2Application;
import it.cambi.dhis2.exception.Dhis2RestClientException;
import it.cambi.dhis2.model.BaseDataElement;
import it.cambi.dhis2.model.DataElementGroup;
import it.cambi.dhis2.model.DataElementGroupWrap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(
    classes = Dhis2Application.class,
    properties = {"spring.redis.embedded=true"})
class DataElementGroupsRepositoryMockTest extends AbstractTest {

  @Autowired private DataElementRepository dataElementRepository;

  @MockBean private RestTemplate restTemplate;

  @Value("${dhis2.api.data.elements.group}")
  private String dhis2ApiDataElementGroups;

  @Test
  public void shouldExecuteRestTemplateAndGetDataElementGroups() {

    when(restTemplate.exchange(
            argumentUrlCaptor.capture(),
            httpMethodCaptor.capture(),
            httpEntityCaptor.capture(),
            clazzDataElementGroupsCaptor.capture(),
            (Object) any()))
        .thenReturn(
            ResponseEntity.status(HttpStatus.OK)
                .body(
                    DataElementGroupWrap.builder()
                        .dataElementGroups(
                            Collections.singletonList(
                                DataElementGroup.builder()
                                    .id(dataElementGroupId)
                                    .displayName(dataElementDisplayName)
                                    .dataElements(
                                        Collections.singletonList(
                                            BaseDataElement.builder().id(dataElementId).build()))
                                    .build()))
                        .build()));

    List<DataElementGroup> dataElementList = dataElementRepository.getDataElementGroups();
    assertNotNull(dataElementList);
    assertEquals(1, dataElementList.size());

    assertTrue(argumentUrlCaptor.getValue().contains(dhis2ApiDataElementGroups));
    assertEquals(HttpMethod.GET, httpMethodCaptor.getValue());
    assertTrue(clazzDataElementGroupsCaptor
            .getValue().isAssignableFrom(DataElementGroupWrap.class));
    assertNotNull(httpEntityCaptor.getValue().getHeaders().get("Authorization"));
  }

  @Test
  public void shouldGetEmptyDataElementGroupWhenNullResponse() {
    when(restTemplate.exchange(
            argumentUrlCaptor.capture(),
            httpMethodCaptor.capture(),
            httpEntityCaptor.capture(),
            clazzDataElementGroupsCaptor.capture(),
            (Object) any()))
        .thenReturn(ResponseEntity.status(HttpStatus.OK).body(null));

    List<DataElementGroup> dataElementList = dataElementRepository.getDataElementGroups();
    assertNotNull(dataElementList);
    assertEquals(0, dataElementList.size());
  }

  @Test
  public void shouldThrowWhenDataElementGroupThrows() {
    when(restTemplate.exchange(
            argumentUrlCaptor.capture(),
            httpMethodCaptor.capture(),
            httpEntityCaptor.capture(),
            clazzDataElementGroupsCaptor.capture(),
            (Object) any()))
        .thenThrow(new RestClientException("Rest client exception"));

    assertThrows(
        Dhis2RestClientException.class,
        () -> dataElementRepository.getDataElementGroups(),
        "Should throw when rest client exception");
  }
}
