package com.aleqsio.data;

import java.time.LocalDate;
import java.util.Date;

/**
 * Interface to allow alternative data services to be used.
 */
public interface IDataService {

    void requestValueOfCurrency(CurrencyData.CurrencyDateTypeKey currencyDateTypeKey);
    void requestTimeRangeOfCurrency(LocalDate startDate, LocalDate endDate, CurrencyData.ExchangeType exchangeType, String currencyCode);
    void requestTimeRangeOfAllCurrencies(LocalDate startDate, LocalDate endDate,CurrencyData.ExchangeType exchangeType);
    /**
     * @return A CurrencyData guaranteed to contain at least all the values requested beforehand. Will hang the thread until values arrive.
     */
   CurrencyData get() throws DataMissingException;
}
