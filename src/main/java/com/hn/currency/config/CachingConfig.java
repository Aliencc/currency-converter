package com.hn.currency.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CachingConfig {
    public static final String CURRENCY_RATES_CACHE = "currencyRate";

    @Bean
    public Caffeine caffeineConfig(@Value("${cache.expirationTime}") Duration expiration) {
        return Caffeine.newBuilder().expireAfterWrite(expiration.getSeconds(), TimeUnit.SECONDS);
    }

    @Bean
    public CacheManager cacheManager(Caffeine caffeine) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager(CURRENCY_RATES_CACHE);
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }
}