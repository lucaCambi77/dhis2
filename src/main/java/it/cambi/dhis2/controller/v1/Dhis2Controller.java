package it.cambi.dhis2.controller.v1;

import io.swagger.annotations.ApiOperation;
import it.cambi.dhis2.dto.DataElementGroupsDto;
import it.cambi.dhis2.dto.DataElementsDto;
import it.cambi.dhis2.service.DataElementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dhis2/v1/api")
@RequiredArgsConstructor
public class Dhis2Controller {

  private final DataElementService dataElementService;

  @ApiOperation(value = "Get Data Elements type from DHIS2", response = DataElementsDto[].class)
  @GetMapping(
      value = "/dataElements",
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public List<DataElementsDto> getDataElements() {
    return dataElementService.getDataElements();
  }

  @ApiOperation(
      value = "Get Data Element Groups type from DHIS2",
      response = DataElementGroupsDto[].class)
  @GetMapping(
      value = "/dataElementGroups",
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public List<DataElementGroupsDto> getDataElementGroups() {
    return dataElementService.getDataElementGroups();
  }
}
