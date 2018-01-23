package com.aleqsio.data.deserialiser;

import com.google.gson.JsonDeserializer;

/**
 * A factory interface to allow passing factory objects to a scheduler.
 */
public interface IDeserialiserFactory {
    JsonDeserializer getDeserialiser(String url);
}
