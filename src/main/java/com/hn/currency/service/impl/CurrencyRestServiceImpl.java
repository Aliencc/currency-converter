package com.hn.currency.service.impl;

import com.hn.currency.model.RateResponse;
import com.hn.currency.model.SymbolsResponse;
import com.hn.currency.service.CurrencyRestService;
import com.hn.currency.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.hn.currency.config.CachingConfig.CURRENCY_RATES_CACHE;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyRestServiceImpl implements CurrencyRestService {
    private final RestTemplate restTemplate;

    @Value("${api.key}")
    private String apiKey;

    @Override
    public Set<String> getSymbols() {
        String url = "/symbols?access_key=" + apiKey;
        SymbolsResponse response = restTemplate.getForObject(url, SymbolsResponse.class);
        return response.getSymbols().keySet();
    }

    @Override
    @Cacheable(value = CURRENCY_RATES_CACHE, key="#source.concat(#target)")
    public BigDecimal getRate(String source, String target) {
        try {
            LOGGER.debug("Get currency rate for {} - {}", source, target);
            String url = "/latest?access_key={accessKey}&base={source}&symbols={target}";
            Map<String, String> params = Map.of("accessKey", apiKey,
                                                "source", source,
                                                "target", target);

            RateResponse response = restTemplate.getForObject(url, RateResponse.class, params);
            return response.getRates().get(target);
        } catch (RuntimeException e) {
            String loggingId = UUID.randomUUID().toString();
            LOGGER.error("LoggingId {} - Error fetching rate: {}", loggingId, e.getMessage(), e);
            throw new ServerException("The error has been logged with id " + loggingId);
        }
    }
}
