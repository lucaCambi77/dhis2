package it.cambi.dhis2.controller.v1;

import it.cambi.dhis2.dto.DataElementGroupsDto;
import it.cambi.dhis2.dto.DataElementsDto;
import it.cambi.dhis2.service.DataElementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dhis2/v1/api")
@RequiredArgsConstructor
public class Dhis2Controller {

  private final DataElementService dataElementService;

  @RequestMapping(
      value = "/dataElements",
      method = RequestMethod.GET,
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public List<DataElementsDto> getDataElements() {
    return dataElementService.getDataElements();
  }

  @RequestMapping(
      value = "/dataElementGroups",
      method = RequestMethod.GET,
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public List<DataElementGroupsDto> getDataElementGroups() {
    return dataElementService.getDataElementGroups();
  }
}
