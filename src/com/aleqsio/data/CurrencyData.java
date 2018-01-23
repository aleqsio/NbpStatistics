package com.aleqsio.data;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A class used for storing values of different currency trades on different dates
 */
public class CurrencyData{
    public HashMap<CurrencyDateTypeKey,Double> data = new HashMap<>();

    public Set<String> getCurrencies() {
        return data.keySet().stream().map(k->k.currencyCode).distinct().collect(Collectors.toSet());
    }

    public List<DataMissingException> encounteredExceptions =new ArrayList<>();

    public void addException(DataMissingException encounteredException) {
     encounteredExceptions.add(encounteredException);
    }
public void clear(){
        data.clear();
        encounteredExceptions.clear();
}
    public CurrencyData joinWith(CurrencyData b) {
        b.data.keySet().stream().forEach(k-> data.putIfAbsent(k,b.data.get(k)));
        encounteredExceptions.addAll(b.encounteredExceptions);
        return this;
    }

    @Override
    public String toString() {
        return "CurrencyData{" +
                "encounteredExceptions=" + encounteredExceptions +
                ", \ndata=" + data +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrencyData that = (CurrencyData) o;

        if (data != null ? !data.equals(that.data) : that.data != null) return false;
        return encounteredExceptions != null ? encounteredExceptions.equals(that.encounteredExceptions) : that.encounteredExceptions == null;
    }

    @Override
    public int hashCode() {
        int result = data != null ? data.hashCode() : 0;
        result = 31 * result + (encounteredExceptions != null ? encounteredExceptions.hashCode() : 0);
        return result;
    }

    public enum ExchangeType {
        AVERAGE,SELL,BUY;

        public static ExchangeType fromParam(String currencyCode) {
            if(Arrays.stream(ExchangeType.values()).noneMatch(k-> k.name().equals(currencyCode))) return ExchangeType.AVERAGE;
            return ExchangeType.valueOf(currencyCode);
        }
    }
    public static class CurrencyDateTypeKey {
        public LocalDate date;
        public String currencyCode; //One of 3 letter currency codes, or 'GOLD'
        public ExchangeType exchangeType;

        public CurrencyDateTypeKey(LocalDate date, String currencyCode, ExchangeType exchangeType) {
            this.date = date;
            this.currencyCode = currencyCode;
            this.exchangeType = exchangeType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CurrencyDateTypeKey that = (CurrencyDateTypeKey) o;

            if (date != null ? !date.equals(that.date) : that.date != null) return false;
            if (currencyCode != null ? !currencyCode.equals(that.currencyCode) : that.currencyCode != null)
                return false;
            return exchangeType == that.exchangeType;
        }

        @Override
        public int hashCode() {
            int result = date != null ? date.hashCode() : 0;
            result = 31 * result + (currencyCode != null ? currencyCode.hashCode() : 0);
            result = 31 * result + (exchangeType != null ? exchangeType.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return currencyCode + " " + exchangeType.toString().toLowerCase()+" value on "+  date;
        }
    }
}
