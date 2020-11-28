package it.cambi.dhis2.repository;

import it.cambi.dhis2.Dhis2Application;
import it.cambi.dhis2.exception.Dhis2RestClientException;
import it.cambi.dhis2.model.BaseDataElement;
import it.cambi.dhis2.model.DataElementGroup;
import it.cambi.dhis2.model.DataElementGroupWrap;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(
    classes = Dhis2Application.class,
    properties = {"spring.redis.embedded=true"})
public class DataElementsRepositoryGroupMockTest {

  @Autowired private DataElementRepository dataElementRepository;

  @MockBean private RestTemplate restTemplate;

  @Value("${dhis2.api.data.elements.group}")
  private String dhis2ApiDataElementGroups;

  private static String dataElementId = "dataElementId";
  private static String dataElementDisplayName = "displayName";
  private static String dataElementGroupId = "dataElementGroupId";

  @Test
  public void shouldGetDataElementGroups() {
    ArgumentCaptor<String> argumentUrl = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<HttpEntity<String>> httpEntity = ArgumentCaptor.forClass(HttpEntity.class);

    when(restTemplate.exchange(
            argumentUrl.capture(), any(), httpEntity.capture(), any(Class.class), (Object) any()))
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

    assertTrue(argumentUrl.getValue().contains(dhis2ApiDataElementGroups));
    assertNotNull(httpEntity.getValue().getHeaders().get("Authorization"));
  }

  @Test
  public void shouldGetEmptyDataElementGroupWhenNullResponse() {
    when(restTemplate.exchange(anyString(), any(), any(), any(Class.class), (Object) any()))
        .thenReturn(ResponseEntity.status(HttpStatus.OK).body(null));

    List<DataElementGroup> dataElementList = dataElementRepository.getDataElementGroups();
    assertNotNull(dataElementList);
    assertEquals(0, dataElementList.size());
  }

  @Test
  public void shouldThrowWhenDataElementGroupThrows() {
    when(restTemplate.exchange(anyString(), any(), any(), any(Class.class), (Object) any()))
            .thenThrow(new RestClientException("Rest client exception"));

    assertThrows(Dhis2RestClientException.class, () ->  dataElementRepository.getDataElementGroups(), "Should throw when rest client exception");
  }
}
