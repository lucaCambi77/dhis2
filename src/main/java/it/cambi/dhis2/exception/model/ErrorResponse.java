package it.cambi.dhis2.exception.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class ErrorResponse {

    private Date timeStamp;
    private Integer status;
    private String error;
    private String errorMessage;
}
