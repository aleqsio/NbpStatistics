package com.aleqsio.data;

import com.aleqsio.asyncQuerying.AsyncSingleQueryProxy;
import com.aleqsio.asyncQuerying.IAsyncSingleQueryServant;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Data service used to divide necessary information into requests, caches data for better performance.
 */
public class CatchedDataService implements IDataService {
    static private final int MAX_DAY_COUNT = 93;
    private AsyncSingleQueryProxy asyncSingleQueryProxy;
    private CurrencyData catchedData = new CurrencyData();

    public CatchedDataService(IAsyncSingleQueryServant iAsyncSingleQueryServant) {
        asyncSingleQueryProxy = new AsyncSingleQueryProxy(iAsyncSingleQueryServant);
    }

    @Override
    public void requestValueOfCurrency(CurrencyData.CurrencyDateTypeKey currencyDateTypeKey) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String url;
        if (currencyDateTypeKey.currencyCode.equals("GOLD"))
            url = "http://api.nbp.pl/api/cenyzlota/" +currencyDateTypeKey.date.format(formatter) + "/?format=json";
        else if (currencyDateTypeKey.exchangeType.equals(CurrencyData.ExchangeType.AVERAGE))
            url = "http://api.nbp.pl/api/exchangerates/rates/a/" + currencyDateTypeKey.currencyCode.toLowerCase() + "/" + currencyDateTypeKey.date.format(formatter) + "/?format=json";
        else {
            url = "http://api.nbp.pl/api/exchangerates/rates/c/" + currencyDateTypeKey.currencyCode.toLowerCase() + "/" + currencyDateTypeKey.date.format(formatter) + "/?format=json";
        }
        asyncSingleQueryProxy.getDataFromUrl(url);
    }

    @Override
    public void requestTimeRangeOfCurrency(LocalDate startDate, LocalDate endDate, CurrencyData.ExchangeType exchangeType, String currencyCode) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int requestcount = (int) Math.ceil((double) ChronoUnit.DAYS.between(startDate, endDate) / MAX_DAY_COUNT);
        if(!currencyCode.equals("GOLD")) {
            String table = exchangeType.equals(CurrencyData.ExchangeType.AVERAGE) ? "a" : "c";

            for (int request = 0; request < requestcount; request++) {
                String url = "http://api.nbp.pl/api/exchangerates/rates/" + table + "/" + currencyCode.toLowerCase() + "/"
                        + (startDate.plusDays(request * MAX_DAY_COUNT)).format(formatter) + "/"
                        + smallerDate(startDate.plusDays((request+1) * MAX_DAY_COUNT-1),endDate).format(formatter)
                        + "/?format=json";
                asyncSingleQueryProxy.getDataFromUrl(url);
            }
        }else{
            for (int request = 0; request < requestcount; request++) {
                String url = "http://api.nbp.pl/api/cenyzlota/"
                        + (startDate.plusDays(request * MAX_DAY_COUNT)).format(formatter) + "/"
                        + smallerDate(startDate.plusDays((request+1) * MAX_DAY_COUNT-1),endDate).format(formatter)
                        + "/?format=json";
                asyncSingleQueryProxy.getDataFromUrl(url);
            }
        }
    }

    @Override
    public void requestTimeRangeOfAllCurrencies(LocalDate startDate, LocalDate endDate, CurrencyData.ExchangeType exchangeType) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String table = exchangeType.equals(CurrencyData.ExchangeType.AVERAGE) ? "a" : "c";
        int requestcount = (int) (ChronoUnit.DAYS.between(startDate, endDate) / MAX_DAY_COUNT);
        for (int request = 0; request <= requestcount; request++) {
            String url = "http://api.nbp.pl/api/exchangerates/tables/"+table+"/"
                    + (startDate.plusDays(request * MAX_DAY_COUNT)).format(formatter) + "/"
                    + smallerDate(startDate.plusDays((request+1) * MAX_DAY_COUNT-1),endDate).format(formatter)
                    + "/?format=json";
            asyncSingleQueryProxy.getDataFromUrl(url);
        }
    }

    @Override
    public CurrencyData get() throws DataMissingException {
        return catchedData.joinWith(asyncSingleQueryProxy.getAllData());
    }
    private static LocalDate smallerDate(LocalDate a, LocalDate b) {
        return a == null ? b : (b == null ? a : (a.isBefore(b) ? a : b));
    }

    public void clearCache() {
        catchedData.clear();
    }
}
