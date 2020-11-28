package it.cambi.dhis2.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@SuperBuilder
@Getter
@Jacksonized
public class DataElementGroup extends BaseDataElement {

    private String displayName;
    private List<BaseDataElement> dataElements;

}
