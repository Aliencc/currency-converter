package com.hn.currency.service.impl;

import com.hn.currency.model.CurrencyRequest;
import com.hn.currency.model.CurrencyResponse;
import com.hn.currency.service.ConverterService;
import com.hn.currency.service.CurrencyRestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyConverterService implements ConverterService<CurrencyRequest, CurrencyResponse> {
    private final CurrencyRestService currencyService;

    @Override
    public CurrencyResponse convert(CurrencyRequest request) {
        String sourceUppercase = request.getSource().trim().toUpperCase(Locale.ROOT);
        String targetUppercase = request.getTarget().trim().toUpperCase(Locale.ROOT);

        BigDecimal rate = currencyService.getRate(sourceUppercase, targetUppercase);
        BigDecimal convertedValue = request.getValue().multiply(rate);
        return new CurrencyResponse(targetUppercase, convertedValue);
    }
}
