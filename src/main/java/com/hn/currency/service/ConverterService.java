package com.hn.currency.service;

public interface ConverterService<T, R> {
    R convert(T request);
}
