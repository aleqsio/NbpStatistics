package com.aleqsio.asyncQuerying;

import com.aleqsio.data.CurrencyData;
import com.aleqsio.data.DataMissingException;

/**
 * Interface to allow alternative Servants to be used.
 */
public interface IAsyncSingleQueryServant {
    CurrencyData getDataFromUrl(String url)  throws DataMissingException;
}
