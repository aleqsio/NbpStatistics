package com.aleqsio.data;

import com.aleqsio.asyncQuerying.AsyncSingleQueryServant;
import com.aleqsio.data.deserialiser.CurrencyGsonDeserialiserFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Aleksander on 19.12.2017.
 */
class CatchedDataServiceTest {
    private CatchedDataService catchedDataService;
    @BeforeEach
    void setUp() {
        catchedDataService = new CatchedDataService(new AsyncSingleQueryServant(new CurrencyGsonDeserialiserFactory()));

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void ShouldReturnCorrectAvgPriceOfGoldOfRangeLongerThan93Days() throws DataMissingException {
        catchedDataService.clearCache();
        catchedDataService.requestTimeRangeOfCurrency(LocalDate.parse("2017-07-13"),LocalDate.parse("2017-12-19"), CurrencyData.ExchangeType.AVERAGE,"GOLD"); //TODO: AVERAGE -> NONE
      Double sum=0d;
        for (Double aDouble : catchedDataService.get().data.values()) {
sum+=aDouble;
        }
        sum/=catchedDataService.get().data.values().size();
        assertTrue(Math.abs(sum-148.554)<0.01);
    }
    @Test
    void ShouldReturnAvgPriceOfTwoDaysOfGold() throws DataMissingException {
        catchedDataService.clearCache();
        catchedDataService.requestTimeRangeOfCurrency(LocalDate.parse("2017-07-03"),LocalDate.parse("2017-07-04"), CurrencyData.ExchangeType.AVERAGE,"GOLD"); //TODO: AVERAGE -> NONE
        Double sum=0d;
        Collection<Double> values = catchedDataService.get().data.values();
        for (Double aDouble : values) {
            sum+=aDouble;
        }
        sum/= values.size();
        assertTrue(Math.abs(sum-147.51)<0.01);
    }
    @Test
    void ShouldReturnAvgPriceOfTwoDaysOfCurrency() throws DataMissingException {
        catchedDataService.clearCache();
        catchedDataService.requestTimeRangeOfCurrency(LocalDate.parse("2016-02-02"),LocalDate.parse("2016-02-03"), CurrencyData.ExchangeType.SELL,"USD"); //TODO: AVERAGE -> NONE
        Double sum=0d;
        for (Double aDouble : catchedDataService.get().data.entrySet().stream().filter(k->k.getKey().exchangeType.equals(CurrencyData.ExchangeType.SELL)).map(Map.Entry::getValue).collect(Collectors.toList())) {

            sum+=aDouble;
        }
        sum/=2;
        assertTrue(Math.abs(sum-4.0699)<0.01);
    }
}