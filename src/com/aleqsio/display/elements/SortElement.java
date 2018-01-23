package com.aleqsio.display.elements;

import com.aleqsio.data.CurrencyData;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Aleksander on 07.01.2018.
 */
public class SortElement implements IDataDisplayElement {
    private LocalDate date;
    private String result;

    public SortElement(LocalDate date) {
        this.date = date;
    }
    @Override
    public String getDisplayReport() {
        return result;
    }

    @Override
    public void executeWithData(CurrencyData currencyData) {
        List<Pair<Map.Entry<CurrencyData.CurrencyDateTypeKey, Double>,Map.Entry<CurrencyData.CurrencyDateTypeKey, Double>>> values = currencyData.data.entrySet().stream()
                .filter(k -> k.getKey().date.equals(date)).filter(k -> k.getKey().exchangeType.equals(CurrencyData.ExchangeType.SELL))
                .map(k -> new Pair<>(k, currencyData.data.entrySet().stream().filter(l -> l.getKey().equals(new CurrencyData.CurrencyDateTypeKey(date, k.getKey().currencyCode, CurrencyData.ExchangeType.BUY))).findAny().get())).collect(Collectors.toList());
    result = values.stream().sorted(Comparator.comparingDouble(k->k.getKey().getValue()-k.getValue().getValue())).map(k->"At difference of "+(k.getKey().getValue()-k.getValue().getValue()) + " currency "+ k.getKey().toString()+", "+k.getValue().toString()).collect(Collectors.joining("\n"));
    }
}
