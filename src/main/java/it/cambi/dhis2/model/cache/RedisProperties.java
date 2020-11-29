package it.cambi.dhis2.model.cache;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RedisProperties {

  private int redisPort;
  private String redisHost;
  private String redisPassword;
}
