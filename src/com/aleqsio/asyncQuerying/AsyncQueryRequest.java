package com.aleqsio.asyncQuerying;

import com.aleqsio.data.CurrencyData;
import com.aleqsio.data.DataMissingException;

import java.util.concurrent.CompletableFuture;

/**
 * A wrapper class for a servant, carries information about a particular request, and gets executed by the scheduler when suitable.
 */
public class AsyncQueryRequest {
    private String url;
    private IAsyncSingleQueryServant iAsyncSingleQueryServant;

    public CompletableFuture<CurrencyData> getFuture() {
        return future;
    }

    private CompletableFuture<CurrencyData> future;

    public AsyncQueryRequest(IAsyncSingleQueryServant iAsyncSingleQueryServant) {
        this.iAsyncSingleQueryServant=iAsyncSingleQueryServant;
        CompletableFuture<CurrencyData> future= CompletableFuture.supplyAsync(() -> {
            try {
                return execute();
            } catch (DataMissingException e) {
                return new CurrencyData();
            }
        });
        this.future = future;
    }

    public void setUrl(String url) {
      this.url=url;
    }
    public CurrencyData execute() throws DataMissingException {
        return iAsyncSingleQueryServant.getDataFromUrl(url);
    }


}
