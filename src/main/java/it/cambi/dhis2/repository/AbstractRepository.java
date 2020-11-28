package it.cambi.dhis2.repository;

import it.cambi.dhis2.exception.Dhis2RestClientException;
import org.springframework.web.client.RestClientException;

import java.util.function.Supplier;

public class AbstractRepository {

  public <T> T exchange(Supplier<T> supplier) {
    try {

      return supplier.get();

    } catch (RestClientException restClientException) {
      throw new Dhis2RestClientException(restClientException);
    }
  }
}
