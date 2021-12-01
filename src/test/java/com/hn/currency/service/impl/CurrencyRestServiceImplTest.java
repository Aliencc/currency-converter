package com.hn.currency.service.impl;

import com.hn.currency.Application;
import com.hn.currency.model.RateResponse;
import com.hn.currency.model.SymbolsResponse;
import com.hn.currency.service.CurrencyRestService;
import com.hn.currency.service.exception.ServerException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class CurrencyRestServiceImplTest {
    @Mock
    private final RestTemplate restTemplate = mock(RestTemplate.class);

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private CurrencyRestService service;

    @Before
    public void setup() {
        service = new CurrencyRestServiceImpl(restTemplate);
        ReflectionTestUtils.setField(service, "apiKey", "key");
    }

    @Test
    public void testGetSymbols() {
        String url = "/symbols?access_key=key";
        SymbolsResponse response = new SymbolsResponse();
        response.setSymbols(Map.of("USD", "US Dollar"));

        when(restTemplate.getForObject(url, SymbolsResponse.class)).thenReturn(response);
        Set<String> result = service.getSymbols();

        assertEquals(1, result.size());
        assertTrue(result.contains("USD"));
    }

    @Test
    public void testGetRate() {
        String target = "USD";
        RateResponse response = new RateResponse();
        response.setRates(Map.of(target, BigDecimal.TEN));

        when(restTemplate.getForObject(anyString(), eq(RateResponse.class), anyMap())).thenReturn(response);
        BigDecimal result = service.getRate("anything", target);

        assertEquals(BigDecimal.TEN, result);
    }

    @Test(expected = ServerException.class)
    public void testGetRate_ServerException() {
        doThrow(new RuntimeException()).when(restTemplate).getForObject(anyString(), eq(RateResponse.class), anyMap());
        service.getRate("anything", "target");
    }
}
