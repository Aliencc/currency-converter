package com.hn.currency.service;

public interface ValidationService<T> {
    void validate(T request);
}
