package it.cambi.dhis2.service;

import it.cambi.dhis2.dto.DataElementGroupsDto;
import it.cambi.dhis2.dto.DataElementsDto;
import it.cambi.dhis2.model.BaseDataElement;
import it.cambi.dhis2.repository.DataElementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataElementService {

  private final DataElementRepository dataElementRepository;

  @Cacheable(value = "dataElementsCache", sync = true)
  public List<DataElementsDto> getDataElements() {

    return dataElementRepository.getDataElements().stream()
        .map(
            d ->
                DataElementsDto.builder()
                    .id(d.getId())
                    .name(d.getDisplayName())
                    .groups(
                        d.getDataElementGroups().stream()
                            .map(BaseDataElement::getId)
                            .collect(Collectors.toList()))
                    .build())
        .collect(Collectors.toList());
  }

  @Cacheable(value = "dataElementGroupsCache", sync = true)
  public List<DataElementGroupsDto> getDataElementGroups() {

    return dataElementRepository.getDataElementGroups().stream()
        .map(
            d ->
                DataElementGroupsDto.builder()
                    .id(d.getId())
                    .name(d.getDisplayName())
                    .members(
                        d.getDataElements().stream()
                            .map(BaseDataElement::getId)
                            .collect(Collectors.toList()))
                    .build())
        .collect(Collectors.toList());
  }
}
