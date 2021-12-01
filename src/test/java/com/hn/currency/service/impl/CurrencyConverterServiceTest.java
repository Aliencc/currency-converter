package com.hn.currency.service.impl;

import com.hn.currency.model.CurrencyRequest;
import com.hn.currency.model.CurrencyResponse;
import com.hn.currency.service.ConverterService;
import com.hn.currency.service.CurrencyRestService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CurrencyConverterServiceTest {
    @Mock
    private final CurrencyRestService currencyService = mock(CurrencyRestService.class);

    private ConverterService<CurrencyRequest, CurrencyResponse> converter;

    @Before
    public void setup() {
        converter = new CurrencyConverterService(currencyService);
    }

    @Test
    public void testConvert() {
        String source = "EUR";
        String target = "VND";
        CurrencyRequest request = new CurrencyRequest(source, target, BigDecimal.ONE);

        when(currencyService.getRate(source, target)).thenReturn(BigDecimal.TEN);
        CurrencyResponse response = converter.convert(request);

        assertEquals(target, response.getUnit());
        assertEquals(BigDecimal.TEN, response.getValue());
    }
}
