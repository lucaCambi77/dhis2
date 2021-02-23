package it.cambi.dhis2.repository;

import it.cambi.dhis2.Dhis2Application;
import it.cambi.dhis2.model.DataElement;
import it.cambi.dhis2.model.DataElementGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
    classes = Dhis2Application.class,
    properties = {"spring.redis.embedded=true"})
class DataElementsRepositoryTest {

  @Autowired private DataElementRepository dataElementRepository;

  @Test
  public void shouldGetDataElements() {

    List<DataElement> dataElementList = dataElementRepository.getDataElements();
    assertNotNull(dataElementList);
    assertTrue(dataElementList.size() > 0);
  }

  @Test
  public void shouldGetDataElementGroups() {

    List<DataElementGroup> dataElementList = dataElementRepository.getDataElementGroups();
    assertNotNull(dataElementList);
    assertTrue(dataElementList.size() > 0);
  }
}
