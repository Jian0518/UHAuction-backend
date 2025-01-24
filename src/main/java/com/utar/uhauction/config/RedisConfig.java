package com.utar.uhauction.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);
        configuration.setPassword(redisPassword);
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, 
                ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(objectMapper);

        // Define different TTLs for different cache names
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // Default configuration
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .disableCachingNullValues();

        // Existing configurations
        cacheConfigurations.put("categoryTrends", defaultConfig.entryTtl(Duration.ofDays(1)));
        cacheConfigurations.put("monthlyStats", defaultConfig.entryTtl(Duration.ofHours(1)));
        cacheConfigurations.put("itemDetails", defaultConfig.entryTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put("itemList", defaultConfig.entryTtl(Duration.ofMinutes(15)));
        cacheConfigurations.put("itemImages", defaultConfig.entryTtl(Duration.ofHours(1)));
        cacheConfigurations.put("allItems", defaultConfig.entryTtl(Duration.ofMinutes(15)));
        
        // New configurations
        cacheConfigurations.put("categories", defaultConfig.entryTtl(Duration.ofHours(12)));
        cacheConfigurations.put("categoryItems", defaultConfig.entryTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put("billboards", defaultConfig.entryTtl(Duration.ofHours(1)));
        cacheConfigurations.put("comments", defaultConfig.entryTtl(Duration.ofMinutes(15)));
        cacheConfigurations.put("links", defaultConfig.entryTtl(Duration.ofHours(24)));
        cacheConfigurations.put("funds", defaultConfig.entryTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put("searchResults", defaultConfig.entryTtl(Duration.ofMinutes(15)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
} 