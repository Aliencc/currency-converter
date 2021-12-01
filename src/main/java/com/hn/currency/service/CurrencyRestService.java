package com.hn.currency.service;

import java.math.BigDecimal;
import java.util.Set;

public interface CurrencyRestService {
    Set<String> getSymbols();
    BigDecimal getRate(String source, String target);
}
