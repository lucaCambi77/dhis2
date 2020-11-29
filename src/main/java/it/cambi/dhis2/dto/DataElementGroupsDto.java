package it.cambi.dhis2.dto;

import it.cambi.dhis2.model.BaseDataElement;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@SuperBuilder
@Getter
@Jacksonized
public class DataElementGroupsDto extends BaseDataElement {

  String name;
  List<String> members;
}
