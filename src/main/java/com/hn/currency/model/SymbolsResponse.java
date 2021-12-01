package com.hn.currency.model;

import lombok.Data;

import java.util.Map;

@Data
public class SymbolsResponse {
    private boolean success;
    private Map<String, String> symbols;
}
