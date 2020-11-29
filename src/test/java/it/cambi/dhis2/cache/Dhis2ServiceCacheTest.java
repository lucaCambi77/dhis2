package it.cambi.dhis2.cache;

import it.cambi.dhis2.EmbeddedRedisConfig;
import it.cambi.dhis2.controller.v1.Dhis2Controller;
import it.cambi.dhis2.dto.DataElementGroupsDto;
import it.cambi.dhis2.dto.DataElementsDto;
import it.cambi.dhis2.model.BaseDataElement;
import it.cambi.dhis2.model.DataElement;
import it.cambi.dhis2.model.DataElementGroup;
import it.cambi.dhis2.repository.DataElementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(
    classes = {EmbeddedRedisConfig.class},
    properties = {"spring.redis.embedded=true"})
public class Dhis2ServiceCacheTest {

  private static String dataElementId = "dataElementId";
  private static String dataElementDisplayName = "displayName";
  private static String dataElementGroupId = "dataElementGroupId";

  @Autowired private Dhis2Controller dhis2Controller;
  @Autowired private CacheManager cacheManager;

  @MockBean private DataElementRepository dataElementRepository;

  @BeforeEach
  public void setUp() {
    invalidateCache();
  }

  @Test
  public void shouldUseCacheToGetDataElements() {
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

    List<DataElementsDto> dataElementsDto = dhis2Controller.getDataElements();

    verify(dataElementRepository).getDataElements();

    reset(dataElementRepository);

    List<DataElementsDto> dataElementsDto1 = dhis2Controller.getDataElements();

    verify(dataElementRepository, times(0)).getDataElements();

    assertEquals(dataElementsDto, dataElementsDto1);
  }

  @Test
  public void shouldNotUseCacheToGetDataElementsAfterCacheInvalidate() {
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

    List<DataElementsDto> dataElementsDto = dhis2Controller.getDataElements();

    verify(dataElementRepository).getDataElements();

    reset(dataElementRepository);

    invalidateCache();

    String dataElementId1 = "dataElementId1";
    String dataElementDisplayName1 = "displayName1";
    String dataElementGroupId1 = "dataElementGroupId1";

    when(dataElementRepository.getDataElements())
        .thenReturn(
            Collections.singletonList(
                DataElement.builder()
                    .id(dataElementId1)
                    .displayName(dataElementDisplayName1)
                    .dataElementGroups(
                        Collections.singletonList(
                            BaseDataElement.builder().id(dataElementGroupId1).build()))
                    .build()));

    List<DataElementsDto> dataElementsDto1 = dhis2Controller.getDataElements();

    verify(dataElementRepository).getDataElements();

    assertNotEquals(dataElementsDto, dataElementsDto1);
  }

  @Test
  public void shouldUseCacheToGetDataElementGroups() {
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

    List<DataElementGroupsDto> dataElementsDto = dhis2Controller.getDataElementGroups();

    verify(dataElementRepository).getDataElementGroups();

    reset(dataElementRepository);

    List<DataElementGroupsDto> dataElementsDto1 = dhis2Controller.getDataElementGroups();

    verify(dataElementRepository, times(0)).getDataElements();

    assertEquals(dataElementsDto, dataElementsDto1);
  }

  @Test
  public void shouldNotUseCacheToGetDataElementGroupsAfterCacheInvalidate() {
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

    List<DataElementGroupsDto> dataElementsDto = dhis2Controller.getDataElementGroups();

    verify(dataElementRepository).getDataElementGroups();

    reset(dataElementRepository);

    invalidateCache();

    String dataElementId1 = "dataElementId1";
    String dataElementDisplayName1 = "displayName1";
    String dataElementGroupId1 = "dataElementGroupId1";

    when(dataElementRepository.getDataElementGroups())
        .thenReturn(
            Collections.singletonList(
                DataElementGroup.builder()
                    .id(dataElementGroupId1)
                    .displayName(dataElementDisplayName1)
                    .dataElements(
                        Collections.singletonList(
                            BaseDataElement.builder().id(dataElementId1).build()))
                    .build()));

    List<DataElementGroupsDto> dataElementsDto1 = dhis2Controller.getDataElementGroups();

    verify(dataElementRepository).getDataElementGroups();

    assertNotEquals(dataElementsDto, dataElementsDto1);
  }

  private void invalidateCache() {
    cacheManager.getCacheNames().stream()
        .map(n -> Optional.ofNullable(cacheManager.getCache(n)))
        .forEach(c -> c.ifPresent(Cache::clear));
  }
}
