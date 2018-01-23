package com.aleqsio.display.elements;

import com.aleqsio.data.CurrencyData;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Aleksander on 07.01.2018.
 */
public class GraphElement implements IDataDisplayElement {
    private LocalDate startDate;
    private LocalDate endDate;
    private String currencyCode;
    private String result;
    private static final int WIDTH=40;

    public GraphElement(LocalDate startDate, LocalDate endDate, String currencyCode) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.currencyCode = currencyCode;
    }
    @Override
    public String getDisplayReport() {
        return result;
    }

    @Override
    public void executeWithData(CurrencyData currencyData) {
        final double min = currencyData.data.entrySet().stream().filter(k -> k.getKey().currencyCode.equals(currencyCode)).mapToDouble(Map.Entry::getValue).min().getAsDouble();
        final double max = currencyData.data.entrySet().stream().filter(k -> k.getKey().currencyCode.equals(currencyCode)).mapToDouble(Map.Entry::getValue).max().getAsDouble();

        result = currencyData.data.entrySet().stream().filter(k -> k.getKey().currencyCode.equals(currencyCode)).sorted(this::compareDatesByWeek).map(k -> dateToWeekNumber(k.getKey().date) +" "+ getBar(k.getValue(), min,max) + " " + k.getValue()).collect(Collectors.joining("\n"));

    }

    private int compareDatesByWeek(Map.Entry<CurrencyData.CurrencyDateTypeKey, Double> o1, Map.Entry<CurrencyData.CurrencyDateTypeKey, Double> o2) {
        if(o1.getKey().date.getDayOfWeek().getValue()!=o2.getKey().date.getDayOfWeek().getValue()) return o1.getKey().date.getDayOfWeek().getValue()-o2.getKey().date.getDayOfWeek().getValue();
        if(o1.getKey().date.getYear()!=o2.getKey().date.getYear()) return o1.getKey().date.getYear()-o2.getKey().date.getYear();
        if(o1.getKey().date.getMonthValue()!=o2.getKey().date.getMonthValue()) return o1.getKey().date.getMonthValue()-o2.getKey().date.getMonthValue();
        return startDate.until(o1.getKey().date).getDays()/7-startDate.until(o2.getKey().date).getDays()/7;
    }

    private String dateToWeekNumber(LocalDate date){
        return date.getYear() +" " + date.getMonth().getDisplayName(TextStyle.SHORT,Locale.ENGLISH) + " "+  String.valueOf(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH))+" "+String.valueOf(startDate.until(date).getDays()/7+1);
    }
    private String getBar(double value, double min, double max){
        return String.join("", Collections.nCopies((int) ((value-min)/(max-min)*WIDTH), "▮"))+String.join("", Collections.nCopies(WIDTH-(int) ((value-min)/(max-min)*WIDTH), "▯"));
    }
}
