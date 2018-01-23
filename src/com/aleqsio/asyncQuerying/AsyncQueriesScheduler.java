package com.aleqsio.asyncQuerying;


import com.aleqsio.data.CurrencyData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Handles scheduling asynchronous requests to api server, merges response {@link CurrencyData} packages as they arrive.
 */
public class AsyncQueriesScheduler {
    private static AsyncQueriesScheduler instance = null;

    private AsyncQueriesScheduler() {
    }

    /**
     * @return A global instance of an AsyncQueriesScheduler.
     */
    public static AsyncQueriesScheduler getInstance() {
        if (instance == null) {
            instance = new AsyncQueriesScheduler();
        }
        return instance;
    }
    private List<AsyncQueryRequest> requests=new ArrayList<>();

    void enqueue(AsyncQueryRequest asyncQueryRequest) {
        requests.add(asyncQueryRequest);
    }

    CurrencyData dispatch() {
        CurrencyData data = requests.stream().map(AsyncQueryRequest::getFuture).map(CompletableFuture::join).reduce(
                new CurrencyData(),
                (a, b) -> {
                    a.joinWith(b);
                    return a;
                });
        requests.clear();
        return data;
    }

}
