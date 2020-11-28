package it.cambi.dhis2.client;

import it.cambi.dhis2.model.BaseDataElement;
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
public class DataElementsGroupClientTest {

  @InjectMocks private DataElementClient dataElementClient;
  @Mock private DataElementRepository dataElementRepository;

  private static String dataElementGroupId = "dataElementGroupId";
  private static String dataElementGroupDisplayName = "displayName";
  private static String dataElementId = "dataElementId";

  @Test
  public void shouldGetDataElements() {
    when(dataElementRepository.getDataElementGroups()).thenReturn(
            Collections.singletonList(DataElementGroup.builder()
                    .id(dataElementGroupId)
                    .displayName(dataElementGroupDisplayName)
                    .dataElements(Collections.singletonList(BaseDataElement.builder().id(dataElementId).build()))
                    .build()));

    List<DataElementGroup> dataElements = dataElementClient.getDataGroupElements();

    assertNotNull(dataElements);
    assertEquals(1, dataElements.size());
    assertEquals(DataElementGroup.builder().id(dataElementGroupId).build(), dataElements.get(0));
    assertEquals(dataElementGroupDisplayName, dataElements.get(0).getDisplayName());
    assertEquals(1, dataElements.get(0).getDataElements().size());
    assertEquals(
        BaseDataElement.builder().id(dataElementId).build(),
        dataElements.get(0).getDataElements().get(0));

    verify(dataElementRepository).getDataElementGroups();
  }
}
