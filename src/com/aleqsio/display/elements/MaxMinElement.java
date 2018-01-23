package com.aleqsio.display.elements;

import com.aleqsio.data.CurrencyData;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by Aleksander on 07.01.2018.
 */
public class MaxMinElement implements IDataDisplayElement {
    private String currencyCode;
    private String result;

    public MaxMinElement(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    @Override
    public String getDisplayReport() {
        return result;
    }

    @Override
    public void executeWithData(CurrencyData currencyData) {
        final Map.Entry<CurrencyData.CurrencyDateTypeKey, Double> minEntry = currencyData.data.entrySet().stream().filter(k -> k.getKey().currencyCode.equals(currencyCode)).min(Comparator.comparingDouble(Map.Entry::getValue)).get();
        final Map.Entry<CurrencyData.CurrencyDateTypeKey, Double> maxEntry = currencyData.data.entrySet().stream().filter(k -> k.getKey().currencyCode.equals(currencyCode)).max(Comparator.comparingDouble(Map.Entry::getValue)).get();
result ="Minimum value at: " +minEntry.toString()+", maximum value at: "+maxEntry.toString();
    }
}
