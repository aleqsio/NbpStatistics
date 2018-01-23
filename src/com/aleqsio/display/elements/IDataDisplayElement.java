package com.aleqsio.display.elements;

import com.aleqsio.data.CurrencyData;

/**
 * An interface to allow different data transformations and display definitions to be declared and used in {@link com.aleqsio.display.DataDisplay}
 */
public interface IDataDisplayElement {
    /**
     * @return a view of data, depending on a particular element.
     */
    String getDisplayReport();

    /**
     * Executes operations on provided data, must be called before getDisplayReport();
     * @param currencyData - data to be operated on.
     */
    void executeWithData(CurrencyData currencyData);
}
