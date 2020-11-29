package it.cambi.dhis2.repository;

import it.cambi.dhis2.Dhis2Application;
import it.cambi.dhis2.exception.Dhis2RestClientException;
import it.cambi.dhis2.model.BaseDataElement;
import it.cambi.dhis2.model.DataElement;
import it.cambi.dhis2.model.DataElementWrap;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
public class DataElementsRepositoryMockTest {

  @Autowired private DataElementRepository dataElementRepository;

  @MockBean private RestTemplate restTemplate;

  @Value("${dhis2.api.data.elements}")
  private String dhis2ApiDataElements;

  private static String dataElementId = "dataElementId";
  private static String dataElementDisplayName = "displayName";
  private static String dataElementGroupId = "dataElementGroupId";

  @Test
  public void shouldExecuteRestTemplateAndGetDataElements() {
    ArgumentCaptor<String> argumentUrl = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<HttpMethod> httpMethod = ArgumentCaptor.forClass(HttpMethod.class);
    ArgumentCaptor<Class<DataElementWrap>> clazz = ArgumentCaptor.forClass(Class.class);
    ArgumentCaptor<HttpEntity<String>> httpEntity = ArgumentCaptor.forClass(HttpEntity.class);

    when(restTemplate.exchange(
            argumentUrl.capture(),
            httpMethod.capture(),
            httpEntity.capture(),
            clazz.capture(),
            (Object) any()))
        .thenReturn(
            ResponseEntity.status(HttpStatus.OK)
                .body(
                    DataElementWrap.builder()
                        .dataElements(
                            Collections.singletonList(
                                DataElement.builder()
                                    .id(dataElementId)
                                    .displayName(dataElementDisplayName)
                                    .dataElementGroups(
                                        Collections.singletonList(
                                            BaseDataElement.builder()
                                                .id(dataElementGroupId)
                                                .build()))
                                    .build()))
                        .build()));

    List<DataElement> dataElementList = dataElementRepository.getDataElements();
    assertNotNull(dataElementList);
    assertEquals(1, dataElementList.size());

    assertTrue(argumentUrl.getValue().contains(dhis2ApiDataElements));
    assertEquals(HttpMethod.GET, httpMethod.getValue());
    assertTrue(clazz.getValue().isAssignableFrom(DataElementWrap.class));
    assertNotNull(httpEntity.getValue().getHeaders().get("Authorization"));
  }

  @Test
  public void shouldGetEmptyDataElementWhenNullResponse() {
    when(restTemplate.exchange(anyString(), any(), any(), any(Class.class), (Object) any()))
        .thenReturn(ResponseEntity.status(HttpStatus.OK).body(null));

    List<DataElement> dataElementList = dataElementRepository.getDataElements();
    assertNotNull(dataElementList);
    assertEquals(0, dataElementList.size());
  }

  @Test
  public void shouldThrowWhenDataElementThrows() {
    when(restTemplate.exchange(anyString(), any(), any(), any(Class.class), (Object) any()))
        .thenThrow(new RestClientException("Rest client exception"));

    assertThrows(
        Dhis2RestClientException.class,
        () -> dataElementRepository.getDataElements(),
        "Should throw when rest client exception");
  }
}
