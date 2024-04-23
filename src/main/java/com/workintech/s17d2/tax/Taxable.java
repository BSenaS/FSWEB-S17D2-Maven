package com.workintech.s17d2.tax;

import org.springframework.stereotype.Component;

@Component
public interface Taxable {
    Double getSimpleTaxRate();
    Double getMiddleTaxRate();
    Double getUpperTaxRate ();
}
