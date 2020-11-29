package it.cambi.dhis2;

import it.cambi.dhis2.model.DataElementGroupWrap;
import it.cambi.dhis2.model.DataElementWrap;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

public abstract class AbstractTest {

  protected static final String dataElementId = "dataElementId";
  protected static final String dataElementDisplayName = "displayName";
  protected static final String dataElementGroupId = "dataElementGroupId";
  protected static final String dataElementGroupDisplayName = "displayName";

  @Captor protected ArgumentCaptor<Class<DataElementWrap>> clazzDataElementsCaptor;
  @Captor protected ArgumentCaptor<Class<DataElementGroupWrap>> clazzDataElementGroupsCaptor;
  @Captor protected ArgumentCaptor<String> argumentUrlCaptor;
  @Captor protected ArgumentCaptor<HttpMethod> httpMethodCaptor;
  @Captor protected ArgumentCaptor<HttpEntity<String>> httpEntityCaptor;
}
