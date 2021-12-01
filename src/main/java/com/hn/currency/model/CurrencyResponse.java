package com.hn.currency.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CurrencyResponse {

    private String unit;
    private BigDecimal value;
}
