package com.hn.currency.controller;

import com.hn.currency.model.CurrencyRequest;
import com.hn.currency.model.CurrencyResponse;
import com.hn.currency.service.ConverterService;
import com.hn.currency.service.ValidationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyRestControllerTest {

    @Mock
    private ValidationService<CurrencyRequest> validator;

    @Mock
    private ConverterService<CurrencyRequest, CurrencyResponse> converter;

    @InjectMocks
    private CurrencyRestController controller;

    @Test
    public void testConvert() {
        CurrencyRequest request = new CurrencyRequest(null, null, null);
        controller.convert(request);
        verify(validator, times(1)).validate(request);
        verify(converter, times(1)).convert(request);
    }
}