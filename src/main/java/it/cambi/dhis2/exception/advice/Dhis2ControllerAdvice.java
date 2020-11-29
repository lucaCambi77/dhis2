package it.cambi.dhis2.exception.advice;

import it.cambi.dhis2.exception.Dhis2RestClientException;
import it.cambi.dhis2.exception.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;

@ControllerAdvice
public class Dhis2ControllerAdvice {

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> illegalArgumentBadRequest(
      IllegalArgumentException illegalArgumentException) {
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .error(httpStatus.getReasonPhrase())
            .errorMessage(illegalArgumentException.getMessage())
            .status(httpStatus.value())
            .timeStamp(new Date())
            .build();

    return ResponseEntity.status(httpStatus).body(errorResponse);
  }

  @ExceptionHandler(Dhis2RestClientException.class)
  public ResponseEntity<ErrorResponse> restClientServerException(
      Dhis2RestClientException restClientException) {
    HttpStatus httpStatus;
    String exceptionMessage;
    if (restClientException.getCause() instanceof HttpClientErrorException) {
      HttpClientErrorException httpClientErrorException =
          (HttpClientErrorException) restClientException.getCause();
      httpStatus = httpClientErrorException.getStatusCode();
      exceptionMessage = httpClientErrorException.getMessage();
    } else if (restClientException.getCause() instanceof HttpClientErrorException) {
      HttpClientErrorException httpClientServerException =
          (HttpClientErrorException) restClientException.getCause();
      httpStatus = httpClientServerException.getStatusCode();
      exceptionMessage = httpClientServerException.getMessage();
    } else {
      httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
      exceptionMessage = restClientException.getMessage();
    }

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .error(httpStatus.getReasonPhrase())
            .errorMessage(exceptionMessage)
            .status(httpStatus.value())
            .timeStamp(new Date())
            .build();

    return ResponseEntity.status(httpStatus).body(errorResponse);
  }
}
