package it.cambi.dhis2.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Getter
@EqualsAndHashCode(of = "id")
@Jacksonized
public class BaseDataElement {
    private String id;
}
