package com.hn.currency.service.impl;

import com.hn.currency.model.CurrencyRequest;
import com.hn.currency.service.CurrencyRestService;
import com.hn.currency.service.ValidationService;
import com.hn.currency.service.exception.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service

public class CurrencyRequestValidationService implements ValidationService<CurrencyRequest> {
    private static final String FILENAME_SYMBOLS = "symbols";
    private final CurrencyRestService currencyService;
    private Set<String> symbols;

    @Autowired
    public CurrencyRequestValidationService(CurrencyRestService currencyService) throws IOException, URISyntaxException {
        this.currencyService = currencyService;

        try {
            symbols = currencyService.getSymbols();
        } catch (RuntimeException e) {
            LOGGER.error("Unable to fetch supported symbols from api, returning default symbols instead.", e);
            symbols = getDefaultSymbols();
        }
    }

    @Scheduled(fixedDelayString = "${api.symbolsUpdatePeriod}")
    public void updateSymbols() {
        try {
            LOGGER.debug("Updating new supported symbols.");
            symbols = currencyService.getSymbols();
        } catch (RuntimeException e) {
            LOGGER.error("Unable to fetch supported symbols from api, using old values instead.", e);
        }
    }

    @Override
    public void validate(CurrencyRequest request) {
        validateSymbol(request.getSource());
        validateSymbol(request.getTarget());
    }

    private void validateSymbol(String symbol) {
        if (!symbols.contains(symbol.trim().toUpperCase(Locale.ROOT))) {
            LOGGER.debug("Invalid symbol {}", symbol);
            throw new InvalidRequestException("Invalid symbol " + symbol);
        }
    }

    private Set<String> getDefaultSymbols() throws IOException, URISyntaxException {
        URI uri = ClassLoader.getSystemResource(FILENAME_SYMBOLS).toURI();

        try (Stream<String> stream = Files.lines(Paths.get(uri))) {
            return stream.collect(Collectors.toSet());
        }
    }
}
