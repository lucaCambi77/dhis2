package it.cambi.dhis2.cache;

import it.cambi.dhis2.model.cache.RedisProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.SocketUtils;

@Configuration
public class RedisPropertiesConfig {

    @Value("${spring.redis.port}")
    private int redisPort;
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.password}")
    private String redisPassword;
    @Value("${spring.redis.embedded}")
    private boolean redisEmbedded;

    @Bean(name = "RedisProperties")
    public RedisProperties getRedisProperties() {
        return RedisProperties.builder().redisHost(redisEmbedded ? "localhost" : redisHost)
                .redisPassword(redisPassword)
                .redisPort(redisEmbedded ? SocketUtils.findAvailableTcpPort() : redisPort).build();
    }
}
