package com.aleqsio.asyncQuerying;

import com.aleqsio.data.CurrencyData;
import com.aleqsio.data.DataMissingException;
import com.aleqsio.data.deserialiser.IDeserialiserFactory;
import com.google.gson.*;


/**
 * A synchronous class that can be run in an async scheduler to download data from url, convert data from json using the provided {@link IDeserialiserFactory}, provide console logging of progress and retries failed requests.
 * */
public class AsyncSingleQueryServant implements IAsyncSingleQueryServant {
    public static int REPEAT_REQUEST_COUNT = 10;
    private IDeserialiserFactory deserialiserFactory;

    /**
     * @param currencyGsonDeserialiserFactory a deserialiser used to parse json got from url.
     */
    public AsyncSingleQueryServant(IDeserialiserFactory currencyGsonDeserialiserFactory) {
        this.deserialiserFactory = currencyGsonDeserialiserFactory;
    }

    /**
     * @param url URL to get data from
     * @return a CurrencyData object containing data from a single url.
     * @throws DataMissingException
     */
    public CurrencyData getDataFromUrl(String url) throws DataMissingException {

        for (int attemptCount = 1; attemptCount <= REPEAT_REQUEST_COUNT; attemptCount++) {

            HttpGetRequest request = new HttpGetRequest(url);
            String data = null;
            try {
                data = request.getDataAsString();
            } catch (DataMissingException e) {
                data = "ERROR";
            }
            if (data!=null && !data.equals("ERROR")) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(CurrencyData.class, deserialiserFactory.getDeserialiser(url));
                Gson gson = gsonBuilder.create();
                try {
                    CurrencyData parsedData = gson.fromJson(data, CurrencyData.class);
                    System.out.print("\u2713");
                    return parsedData;
                } catch (JsonSyntaxException e) {
                    //   e.printStackTrace();
                    System.out.print("\u21BB");
                }
            } else {
                System.out.print("\u21BB");
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.print("\u26A0");
        System.out.println("\n"+url + " url failed");

        CurrencyData currencyData = new CurrencyData();
        currencyData.addException(new DataMissingException(DataMissingException.dataMissingExceptionType.NETWORK_ERROR, url, "Failed to load correct json from url"));
        return currencyData;
    }

}