package com.hn.currency.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CurrencyRequest {
    @NotBlank
    private String source;

    @NotBlank
    private String target;

    @Positive
    private BigDecimal value;
}
