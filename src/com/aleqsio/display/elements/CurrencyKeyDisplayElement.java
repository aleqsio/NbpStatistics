package com.aleqsio.display.elements;

import com.aleqsio.data.CurrencyData;

/**
 * Created by Aleksander on 03.01.2018.
 */
public class CurrencyKeyDisplayElement implements IDataDisplayElement {
    private String result;

    public CurrencyKeyDisplayElement(CurrencyData.CurrencyDateTypeKey currencyDateTypeKey) {
        this.key=currencyDateTypeKey;
    }

    public CurrencyData.CurrencyDateTypeKey getKey() {
        return key;
    }
    public void setKey(CurrencyData.CurrencyDateTypeKey key) {
        this.key = key;
    }
    private CurrencyData.CurrencyDateTypeKey key;

    public String getDisplayReport() {
        return result;
    }

    @Override
    public void executeWithData(CurrencyData currencyData) {
        result= key.toString() +": " + currencyData.data.get(key).toString();
    }
}
