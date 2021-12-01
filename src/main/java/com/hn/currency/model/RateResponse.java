package com.hn.currency.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class RateResponse {
    private boolean success;
    private long timestamp;
    private String base;
    private Map<String, BigDecimal> rates;
}
