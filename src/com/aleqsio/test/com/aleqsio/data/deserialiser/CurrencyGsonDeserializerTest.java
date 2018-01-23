package com.aleqsio.data.deserialiser;

import com.aleqsio.data.CurrencyData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Aleksander on 19.12.2017.
 */
class CurrencyGsonDeserializerTest {
    @Test
    void deserialiseShouldReturnCorrectlyParsedJson() {
        CurrencyGsonDeserializer deserializer= new CurrencyGsonDeserializer("cenyzlota");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(CurrencyData.class,deserializer);
            Gson gson = gsonBuilder.create();
        CurrencyData expectedData = new CurrencyData();
        expectedData.data.put(new CurrencyData.CurrencyDateTypeKey(LocalDate.parse("2016-04-04"),"GOLD", CurrencyData.ExchangeType.AVERAGE),0.5d);
        assertEquals(expectedData,gson.fromJson("[{\"data\":\"2016-04-04\",\"cena\":0.5}]", CurrencyData.class));
        CurrencyData currencyData = new CurrencyData();
    }

}