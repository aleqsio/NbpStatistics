package com.aleqsio.asyncQuerying;


import com.aleqsio.data.CurrencyData;

import java.util.concurrent.CompletableFuture;

/**
 * A proxy that handles communication with the server for all data services.
 */
public class AsyncSingleQueryProxy {
    private AsyncQueriesScheduler asyncQueriesScheduler = AsyncQueriesScheduler.getInstance();
private IAsyncSingleQueryServant iAsyncSingleQueryServant;

    public AsyncSingleQueryProxy(IAsyncSingleQueryServant iAsyncSingleQueryServant) {
        this.iAsyncSingleQueryServant = iAsyncSingleQueryServant;
    }

    /**
     * Stages data for download, does not provide it immediately.
     * @param url -url to pull all data from, with a provided servant.
     */
    public void getDataFromUrl(String url) {
        CompletableFuture<CurrencyData> future = new CompletableFuture<>();
        AsyncQueryRequest asyncQueryRequest = new AsyncQueryRequest(iAsyncSingleQueryServant);
        asyncQueryRequest.setUrl(url);
        asyncQueriesScheduler.enqueue(asyncQueryRequest);
    }

    /**
     * Gets all data, locks the thread until all the requests return a result.
     * @return a {@link CurrencyData} that has <b>at least</b> the expected data or an error code.
     */
    public CurrencyData getAllData(){
       return asyncQueriesScheduler.dispatch();
    }
}
