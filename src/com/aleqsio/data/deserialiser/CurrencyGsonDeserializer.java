package com.aleqsio.data.deserialiser;

import com.aleqsio.data.CurrencyData;
import com.aleqsio.data.DataMissingException;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Deserialises JSON according to api.nbp.pl rules. Implements GSON Deserialiser interface
 */
public class CurrencyGsonDeserializer implements JsonDeserializer{
    private final String url;

    public CurrencyGsonDeserializer(String url) {
        this.url=url;
    }

    @Override
        public CurrencyData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        CurrencyData currencyData = new CurrencyData();

            if(url.contains("cenyzlota")){
                for(JsonElement obj: json.getAsJsonArray()){
                    currencyData.data.put(new CurrencyData.CurrencyDateTypeKey(LocalDate.parse(obj.getAsJsonObject().get("data").getAsString()),"GOLD", CurrencyData.ExchangeType.AVERAGE),obj.getAsJsonObject().get("cena").getAsDouble());
                }
            }else if(url.contains("exchangerates/rates")) {
                String code = json.getAsJsonObject().get("code").getAsString();
                for (JsonElement obj : json.getAsJsonObject().get("rates").getAsJsonArray()) {
                    Map<CurrencyData.CurrencyDateTypeKey, Double> keys = new HashMap<>();
                    LocalDate date = LocalDate.parse(obj.getAsJsonObject().get("effectiveDate").getAsString());
                    if (obj.getAsJsonObject().has("mid")) {
                        keys.put(new CurrencyData.CurrencyDateTypeKey(date, code, CurrencyData.ExchangeType.AVERAGE), obj.getAsJsonObject().get("mid").getAsDouble());
                    }
                    if (obj.getAsJsonObject().has("bid")) {
                        keys.put(new CurrencyData.CurrencyDateTypeKey(date, code, CurrencyData.ExchangeType.BUY), obj.getAsJsonObject().get("bid").getAsDouble());
                    }
                    if (obj.getAsJsonObject().has("ask")) {
                        keys.put(new CurrencyData.CurrencyDateTypeKey(date, code, CurrencyData.ExchangeType.SELL), obj.getAsJsonObject().get("ask").getAsDouble());
                    }
                    currencyData.data.putAll(keys);
                }
            }else if(url.contains("exchangerates/tables")){
                Map<CurrencyData.CurrencyDateTypeKey, Double> keys = new HashMap<>();
                for (JsonElement day : json.getAsJsonArray()) {
                    LocalDate date = LocalDate.parse(day.getAsJsonObject().get("effectiveDate").getAsString());
                    for (JsonElement rate : day.getAsJsonObject().get("rates").getAsJsonArray()) {
                        String code = rate.getAsJsonObject().get("code").getAsString();
                        if (rate.getAsJsonObject().has("mid")) {
                            keys.put(new CurrencyData.CurrencyDateTypeKey(date, code, CurrencyData.ExchangeType.AVERAGE), rate.getAsJsonObject().get("mid").getAsDouble());
                        }
                        if (rate.getAsJsonObject().has("bid")) {
                            keys.put(new CurrencyData.CurrencyDateTypeKey(date, code, CurrencyData.ExchangeType.BUY), rate.getAsJsonObject().get("bid").getAsDouble());
                        }
                        if (rate.getAsJsonObject().has("ask")) {
                            keys.put(new CurrencyData.CurrencyDateTypeKey(date, code, CurrencyData.ExchangeType.SELL), rate.getAsJsonObject().get("ask").getAsDouble());
                        }
                    }
                }
                currencyData.data.putAll(keys);
        }else{
            currencyData.addException(new DataMissingException(DataMissingException.dataMissingExceptionType.INVALID_REQUEST_ERROR,url,"no parse pattern found for url"));
        }

            return currencyData;
        }


}
