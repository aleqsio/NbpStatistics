package com.aleqsio.display.elements;

import com.aleqsio.data.CurrencyData;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Created by Aleksander on 04.01.2018.
 */
public class VariationDisplayElement implements IDataDisplayElement {
    private LocalDate startDate;
    private LocalDate endDate;
    private CurrencyData.ExchangeType exchangeType;
    HashMap<String,Double> differences = new HashMap<>();
    String raport="";
    public VariationDisplayElement(LocalDate startDate, LocalDate endDate, CurrencyData.ExchangeType exchangeType) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.exchangeType = exchangeType;
    }

    @Override
    public String getDisplayReport() {
        Map.Entry<String, Double> stringDoubleEntry = differences.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue)).get();
        return raport+stringDoubleEntry.getKey() + " is the currency with biggest divergency of "+stringDoubleEntry.getValue();

    }

    @Override
    public void executeWithData(CurrencyData currencyData) {

        for(String currencyCode:currencyData.getCurrencies()){
            Stream<Map.Entry<CurrencyData.CurrencyDateTypeKey, Double>> entryStream = currencyData.data.entrySet().stream().filter(k -> !k.getKey().date.isBefore(startDate) && !k.getKey().date.isAfter(endDate));
            double min = entryStream.filter(k-> Objects.equals(k.getKey().currencyCode, currencyCode)).min(Comparator.comparingDouble(Map.Entry::getValue)).get().getValue();
            Stream<Map.Entry<CurrencyData.CurrencyDateTypeKey, Double>> entryStream2 = currencyData.data.entrySet().stream().filter(k -> !k.getKey().date.isBefore(startDate) && !k.getKey().date.isAfter(endDate));
            double max = entryStream2.filter(k-> Objects.equals(k.getKey().currencyCode, currencyCode)).max(Comparator.comparingDouble(Map.Entry::getValue)).get().getValue();
            raport+=currencyCode+": min"+min+" max"+max+"\n";
            differences.put(currencyCode,max-min);
        }
    }
}
