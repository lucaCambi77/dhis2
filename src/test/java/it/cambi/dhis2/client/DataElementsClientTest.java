package it.cambi.dhis2.client;

import it.cambi.dhis2.AbstractTest;
import it.cambi.dhis2.model.BaseDataElement;
import it.cambi.dhis2.model.DataElement;
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
public class DataElementsClientTest extends AbstractTest {

  @InjectMocks private DataElementClient dataElementClient;
  @Mock private DataElementRepository dataElementRepository;

  @Test
  public void shouldGetDataElements() {
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

    List<DataElement> dataElements = dataElementClient.getDataElements();

    assertNotNull(dataElements);
    assertEquals(1, dataElements.size());
    assertEquals(DataElement.builder().id(dataElementId).build(), dataElements.get(0));
    assertEquals(dataElementDisplayName, dataElements.get(0).getDisplayName());
    assertEquals(1, dataElements.get(0).getDataElementGroups().size());
    assertEquals(
        BaseDataElement.builder().id(dataElementGroupId).build(),
        dataElements.get(0).getDataElementGroups().get(0));

    verify(dataElementRepository).getDataElements();
  }
}
