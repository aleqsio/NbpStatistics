package com.aleqsio.data.deserialiser;

import com.google.gson.JsonDeserializer;

public class CurrencyGsonDeserialiserFactory implements IDeserialiserFactory{
    /**
     * @param url - url to deserialise
     * @return A CurrencyGsonDeserialiser instantiated with a given url.
     */
    @Override
    public JsonDeserializer getDeserialiser(String url) {
        return new CurrencyGsonDeserializer(url);
    }
}
