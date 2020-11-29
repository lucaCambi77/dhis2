package it.cambi.dhis2.service;

import it.cambi.dhis2.dto.DataElementGroupsDto;
import it.cambi.dhis2.dto.DataElementsDto;
import it.cambi.dhis2.model.BaseDataElement;
import it.cambi.dhis2.model.DataElement;
import it.cambi.dhis2.model.DataElementGroup;
import it.cambi.dhis2.repository.DataElementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DataElementServiceTest {

  @InjectMocks private DataElementService dataElementService;

  @Mock private DataElementRepository dataElementRepository;

  private static String dataElementId = "dataElementId";
  private static String dataElementDisplayName = "displayName";
  private static String dataElementGroupId = "dataElementGroupId";

  @Test
  public void shouldCallRepositoryAndGetGroupElements() {
    when(dataElementRepository.getDataElements())
        .thenReturn(
            Collections.singletonList(
                DataElement.builder()
                    .id(dataElementId)
                    .displayName(dataElementDisplayName)
                    .dataElementGroups(
                        Collections.singletonList(
                            BaseDataElement.builder().id(dataElementGroupId).build()))
                    .build()));

    List<DataElementsDto> dataElementsDto = dataElementService.getDataElements();

    assertNotNull(dataElementsDto);
    assertEquals(1, dataElementsDto.size());
    verify(dataElementRepository).getDataElements();
  }

  @Test
  public void shouldCallRepositoryAndGetElementGroups() {
    when(dataElementRepository.getDataElementGroups())
            .thenReturn(
                    Collections.singletonList(
                            DataElementGroup.builder()
                                    .id(dataElementGroupId)
                                    .displayName(dataElementDisplayName)
                                    .dataElements(
                                            Collections.singletonList(
                                                    BaseDataElement.builder().id(dataElementId).build()))
                                    .build()));

    List<DataElementGroupsDto> dataElementsDto = dataElementService.getDataElementGroups();

    assertNotNull(dataElementsDto);
    assertEquals(1, dataElementsDto.size());
    verify(dataElementRepository).getDataElementGroups();
  }
}
