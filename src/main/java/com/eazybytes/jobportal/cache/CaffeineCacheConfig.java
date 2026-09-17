package com.eazybytes.jobportal.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineCacheConfig {


    @Value("${cache.jobs.ttl-minutes:10}")
    private int jobCacheTtlMinutes;

    @Value("${cache.jobs.max-size:5000}")
    private int jobCacheMaxSize;

    @Value("${cache.companies.ttl-minutes:10}")
    private int companiesCacheTtlMinutes;

    @Value("${cache.companies.max-size:500}")
    private int companiesMaxSize;

    @Value("${cache.roles.ttl-days:1}")
    private int rolesCacheTtlDays;

    @Value("${cache.roles.max-size:100}")
    private int rolesCacheMaxSize;


    @Bean
    public CacheManager coffeineCacheManager() {

        CaffeineCache jobsCache = new CaffeineCache("jobs",
                Caffeine.newBuilder()
                        .expireAfterWrite(jobCacheTtlMinutes, TimeUnit.MINUTES)
                        .maximumSize(jobCacheMaxSize)
                        .build());

        CaffeineCache companiesCache = new CaffeineCache("companies",
                Caffeine.newBuilder()
                        .expireAfterWrite(companiesCacheTtlMinutes, TimeUnit.MINUTES)
                        .maximumSize(companiesMaxSize)
                        .build());

        CaffeineCache  rolesCache = new CaffeineCache("roles",
                Caffeine.newBuilder()
                        .expireAfterWrite(rolesCacheTtlDays, TimeUnit.DAYS)
                        .maximumSize(rolesCacheMaxSize)
                        .build());

        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Arrays.asList(jobsCache,companiesCache,rolesCache));
        return manager;
    }
}
