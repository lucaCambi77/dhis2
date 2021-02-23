package it.cambi.dhis2.controller;

import it.cambi.dhis2.AbstractTest;
import it.cambi.dhis2.Dhis2Application;
import it.cambi.dhis2.dto.DataElementGroupsDto;
import it.cambi.dhis2.dto.DataElementsDto;
import it.cambi.dhis2.exception.Dhis2RestClientException;
import it.cambi.dhis2.service.DataElementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
    classes = Dhis2Application.class,
    properties = {"spring.redis.embedded=true"})
@AutoConfigureMockMvc
class Dhis2ControllerTest extends AbstractTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private WebApplicationContext context;

  @MockBean private DataElementService dataElementService;

  private final MediaType mediaType = MediaType.APPLICATION_JSON;

  @Value("${dhis2.api.username}")
  private String dhis2Username;

  @Value("${dhis2.api.password}")
  private String dhis2Password;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  @Test
  public void shouldCallGetElementsEndpointWithSuccess() throws Exception {

    when(dataElementService.getDataElements())
        .thenReturn(
            Collections.singletonList(
                DataElementsDto.builder()
                    .id(dataElementId)
                    .name(dataElementDisplayName)
                    .groups(Collections.singletonList(dataElementGroupId))
                    .build()));

    mockMvc
        .perform(
            get("/dhis2/v1/api/dataElements")
                .contentType(mediaType)
                .with(httpBasic(dhis2Username, dhis2Password)))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.[0].id").value(dataElementId))
        .andExpect(jsonPath("$.[0].name").value(dataElementDisplayName))
        .andExpect(jsonPath("$.[0].groups").isArray())
        .andExpect(jsonPath("$.[0].groups", hasSize(1)))
        .andExpect(jsonPath("$.[0].groups[0]").value(dataElementGroupId))
        .andReturn();
  }

  @Test
  public void shouldCallGetElementGroupsEndpointWithSuccess() throws Exception {

    when(dataElementService.getDataElementGroups())
        .thenReturn(
            Collections.singletonList(
                DataElementGroupsDto.builder()
                    .id(dataElementGroupId)
                    .name(dataElementDisplayName)
                    .members(Collections.singletonList(dataElementId))
                    .build()));

    mockMvc
        .perform(
            get("/dhis2/v1/api/dataElementGroups")
                .contentType(mediaType)
                .with(httpBasic(dhis2Username, dhis2Password)))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.[0].id").value(dataElementGroupId))
        .andExpect(jsonPath("$.[0].name").value(dataElementDisplayName))
        .andExpect(jsonPath("$.[0].members").isArray())
        .andExpect(jsonPath("$.[0].members", hasSize(1)))
        .andExpect(jsonPath("$.[0].members[0]").value(dataElementId))
        .andReturn();
  }

  @Test
  public void shouldGetNotAuthorizedWhenCredentialNotProvided() throws Exception {

    mockMvc
        .perform(get("/dhis2/v1/api/dataElements").contentType(mediaType))
        .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()))
        .andReturn();
    verify(dataElementService, times(0)).getDataElements();
  }

  @Test
  public void shouldGetInternalServerErrorHttpServerException() throws Exception {

    when(dataElementService.getDataElements())
        .thenThrow(
            new Dhis2RestClientException(
                new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE)));

    mockMvc
        .perform(
            get("/dhis2/v1/api/dataElements")
                .contentType(mediaType)
                .with(httpBasic(dhis2Username, dhis2Password)))
        .andExpect(status().isServiceUnavailable())
        .andReturn();
    verify(dataElementService).getDataElements();
  }

  @Test
  public void shouldGetInternalClientErrorHttpServerException() throws Exception {

    when(dataElementService.getDataElements())
        .thenThrow(
            new Dhis2RestClientException(new HttpClientErrorException(HttpStatus.BAD_REQUEST)));

    mockMvc
        .perform(
            get("/dhis2/v1/api/dataElements")
                .contentType(mediaType)
                .with(httpBasic(dhis2Username, dhis2Password)))
        .andExpect(status().is4xxClientError())
        .andReturn();
    verify(dataElementService).getDataElements();
  }

  @Test
  public void shouldGetInternalServerErrorRestClientException() throws Exception {

    when(dataElementService.getDataElements())
        .thenThrow(new Dhis2RestClientException(new RestClientException("Some rest client error")));

    mockMvc
        .perform(
            get("/dhis2/v1/api/dataElements")
                .contentType(mediaType)
                .with(httpBasic(dhis2Username, dhis2Password)))
        .andExpect(status().isInternalServerError())
        .andReturn();
    verify(dataElementService).getDataElements();
  }
}
