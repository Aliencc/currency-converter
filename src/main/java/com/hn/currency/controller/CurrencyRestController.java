package com.hn.currency.controller;

import com.hn.currency.model.CurrencyRequest;
import com.hn.currency.model.CurrencyResponse;
import com.hn.currency.service.ConverterService;
import com.hn.currency.service.ValidationService;
import com.hn.currency.tracking.TrackDuration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Async("customTaskExecutor")
@RestController
@RequestMapping("currency")
@RequiredArgsConstructor
public class CurrencyRestController {
    private final ValidationService<CurrencyRequest> validator;
    private final ConverterService<CurrencyRequest, CurrencyResponse> converter;

    /**
     * Currency conversion endpoint
     */
    @PostMapping("convert")
    @TrackDuration
    public CompletableFuture<CurrencyResponse> convert(@Valid @RequestBody CurrencyRequest request) {
        LOGGER.info("Currency converting request: {}", request);
        validator.validate(request);
        return CompletableFuture.completedFuture(converter.convert(request));
    }
}