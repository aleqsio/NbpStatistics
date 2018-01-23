package com.aleqsio.display.elements;

import com.aleqsio.data.CurrencyData;

import java.time.LocalDate;

/**
 * Created by Aleksander on 03.01.2018.
 */
public class CurrencyRangeAveragePriceDisplayElement implements IDataDisplayElement {
    private LocalDate startDate;
    private LocalDate endDate;
    private CurrencyData.ExchangeType exchangeType;
    private String currencyCode;
private double average;
    public CurrencyRangeAveragePriceDisplayElement(LocalDate startDate, LocalDate endDate, CurrencyData.ExchangeType exchangeType, String currencyCode) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.exchangeType = exchangeType;
        this.currencyCode = currencyCode;
    }

    @Override
    public String getDisplayReport() {
        return "average price of "+currencyCode +" "+exchangeType+" value was: "+average;
    }

    @Override
    public void executeWithData(CurrencyData currencyData) {
        double sum=0;
        int count=0;
        for(int day=0;day<=startDate.until(endDate).getDays();day++){
            if(currencyData.data.containsKey(new CurrencyData.CurrencyDateTypeKey(startDate.plusDays(day),currencyCode,exchangeType))){
           sum+= currencyData.data.get(new CurrencyData.CurrencyDateTypeKey(startDate.plusDays(day),currencyCode,exchangeType));
           count++;
            }
        }
        average=sum/count;
    }
}
