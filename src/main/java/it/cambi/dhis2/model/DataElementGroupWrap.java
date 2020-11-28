package it.cambi.dhis2.model;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Getter
@Jacksonized
public class DataElementGroupWrap {

  private List<DataElementGroup> dataElementGroups;
}
