package com.aleqsio.display.elements;

import com.aleqsio.data.CurrencyData;
import com.aleqsio.display.elements.IDataDisplayElement;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;

/**
 * Created by Aleksander on 07.01.2018.
 */
public class CheapestCurrencyElement implements IDataDisplayElement {
    private LocalDate date;
private String result;
    public CheapestCurrencyElement(LocalDate date) {
        this.date = date;
    }

    @Override
    public String getDisplayReport() {
        return result;
    }

    @Override
    public void executeWithData(CurrencyData currencyData) {
        if (currencyData.data.entrySet().stream().anyMatch(k -> k.getKey().date.equals(date) && k.getKey().exchangeType.equals(CurrencyData.ExchangeType.BUY)))
        {
            Map.Entry<CurrencyData.CurrencyDateTypeKey, Double> currencyDateTypeKeyDoubleEntry = currencyData.data.entrySet().stream().filter(k -> k.getKey().date.equals(date) && k.getKey().exchangeType.equals(CurrencyData.ExchangeType.BUY)).min(Comparator.comparingDouble(Map.Entry::getValue)).get();
            result = currencyDateTypeKeyDoubleEntry.getKey().toString() + " is the cheapest currency at " + currencyDateTypeKeyDoubleEntry.getValue();
        }else{
            result="no currencies found for given params";
        }
    }
}
