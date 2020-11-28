package it.cambi.dhis2.exception;

import org.springframework.web.client.RestClientException;

public class Dhis2RestClientException extends RuntimeException {
  public Dhis2RestClientException(RestClientException restClientException) {
    super(restClientException);
  }
}
