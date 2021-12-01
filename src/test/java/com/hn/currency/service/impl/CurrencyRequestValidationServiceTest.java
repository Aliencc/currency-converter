package com.hn.currency.service.impl;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.hn.currency.model.CurrencyRequest;
import com.hn.currency.service.CurrencyRestService;
import com.hn.currency.service.ValidationService;
import com.hn.currency.service.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyRequestValidationServiceTest {
    @Mock
    private final CurrencyRestService currencyService = mock(CurrencyRestService.class);

    private ValidationService<CurrencyRequest> validator;
    private ListAppender<ILoggingEvent> listAppender;

    @Before
    public void setup() {
        Logger logger = (Logger) LoggerFactory.getLogger(CurrencyRequestValidationService.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    @After
    public void teardown() {
        listAppender.list.clear();
    }

    @Test
    public void testValidate() throws IOException, URISyntaxException {
        when(currencyService.getSymbols()).thenThrow(new RuntimeException());

        validator = new CurrencyRequestValidationService(currencyService);
        validator.validate(new CurrencyRequest("  EUR    ", "UsD", BigDecimal.TEN));

        List<ILoggingEvent> logsList = listAppender.list;
        assertEquals(1, logsList.size());
        assertTrue(logsList.get(0).getMessage().contains("returning default symbols instead."));
    }

    @Test (expected = InvalidRequestException.class)
    public void testValidate_InvalidSymbol() throws IOException, URISyntaxException  {
        when(currencyService.getSymbols()).thenReturn(Set.of("EUR"));

        validator = new CurrencyRequestValidationService(currencyService);
        validator.validate(new CurrencyRequest("EUR", "USD", BigDecimal.TEN));
    }
}
