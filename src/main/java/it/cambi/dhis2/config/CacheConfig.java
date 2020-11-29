package it.cambi.dhis2.config;

import it.cambi.dhis2.model.cache.RedisProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
@Import(value = RedisPropertiesConfig.class)
@RequiredArgsConstructor
@Slf4j
public class CacheConfig {

  @Value("${dhis2.dataElementsCache}")
  private Duration dataElementsCache;

  @Value("${dhis2.dataElementGroupsCache}")
  private Duration dataElementGroupsCache;

  private final RedisProperties redisProperties;

  @Bean
  public RedisTemplate<?, ?> getRedisTemplate() {
    RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
    template.setConnectionFactory(jedisConnectionFactory());
    return template;
  }

  @Bean
  JedisConnectionFactory jedisConnectionFactory() {
    log.info(
        "Redis start at "
            + redisProperties.getRedisHost()
            + ", port "
            + redisProperties.getRedisPort());

    RedisStandaloneConfiguration redisStandaloneConfiguration =
        new RedisStandaloneConfiguration(
            redisProperties.getRedisHost(), redisProperties.getRedisPort());
    return new JedisConnectionFactory(redisStandaloneConfiguration);
  }

  @Bean
  public CacheManager getCacheManager(RedisConnectionFactory redisConnectionFactory) {
    return RedisCacheManager.builder(redisConnectionFactory)
        .withInitialCacheConfigurations(getCacheExpiresMap())
        .build();
  }

  private Map<String, RedisCacheConfiguration> getCacheExpiresMap() {
    Map<String, RedisCacheConfiguration> result = new HashMap<>();

    result.put("dataElementsCache", getTtlCacheConfiguration(dataElementsCache));
    result.put("dataElementGroupsCache", getTtlCacheConfiguration(dataElementGroupsCache));

    return result;
  }

  private RedisCacheConfiguration getTtlCacheConfiguration(Duration ttl) {
    return RedisCacheConfiguration.defaultCacheConfig()
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()))
        .entryTtl(ttl)
        .disableCachingNullValues();
  }
}
