package it.cambi.dhis2.client;

import it.cambi.dhis2.model.DataElement;
import it.cambi.dhis2.model.DataElementGroup;
import it.cambi.dhis2.repository.DataElementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataElementClient {

  private final DataElementRepository dataElementRepository;

  public List<DataElement> getDataElements() {

    return dataElementRepository.getDataElements();
  }

  public List<DataElementGroup> getDataGroupElements() {

    return dataElementRepository.getDataElementGroups();
  }
}
