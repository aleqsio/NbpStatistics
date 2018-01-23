package com.aleqsio.asyncQuerying;

import com.aleqsio.data.CurrencyData;
import com.aleqsio.data.deserialiser.CurrencyGsonDeserialiserFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Created by Aleksander on 19.12.2017.
 */
class AsyncSingleQueryProxyTest {
    AsyncSingleQueryProxy mockAsyncQueryProxy;
    private AsyncSingleQueryProxy realAsyncQueryProxy;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        mockAsyncQueryProxy = new AsyncSingleQueryProxy(url -> {
            CurrencyData d =new CurrencyData();
            d.data.put(new CurrencyData.CurrencyDateTypeKey(LocalDate.parse("2016-02-02"),"TEST", CurrencyData.ExchangeType.AVERAGE),0.5);
            return d;
        });
        realAsyncQueryProxy = new AsyncSingleQueryProxy(new AsyncSingleQueryServant(new CurrencyGsonDeserialiserFactory()));
    }


    @org.junit.jupiter.api.Test
    void getAllDataShouldReturnCorrectCurrencyData() {
        mockAsyncQueryProxy.getDataFromUrl("testurl");
        CurrencyData data = mockAsyncQueryProxy.getAllData();
        assertEquals(0.5d,(double)data.data.get(new CurrencyData.CurrencyDateTypeKey(LocalDate.parse("2016-02-02"),"TEST", CurrencyData.ExchangeType.AVERAGE)));
    }

    @Test
    void shouldCorrectlyReturnDataFromAPI() {
        realAsyncQueryProxy.getDataFromUrl("http://api.nbp.pl/api/cenyzlota/2014-05-05");
        CurrencyData data = realAsyncQueryProxy.getAllData();
        assertEquals(125.12d,(double)data.data.get(new CurrencyData.CurrencyDateTypeKey(LocalDate.parse("2014-05-05"),"GOLD", CurrencyData.ExchangeType.AVERAGE)));

    }
}