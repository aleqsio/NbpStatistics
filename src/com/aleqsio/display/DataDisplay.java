package com.aleqsio.display;

import com.aleqsio.data.CurrencyData;
import com.aleqsio.display.elements.IDataDisplayElement;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * A class used for joining {@link IDataDisplayElement} into one string
 */
public class DataDisplay {
    private ArrayList<IDataDisplayElement> displayElements = new ArrayList<>();

    public void addElement(IDataDisplayElement iDataDisplayElement) {
        displayElements.add(iDataDisplayElement);
    }

    public String getDisplayOfData(CurrencyData data) {
        if (data.encounteredExceptions.size() > 0) {
            return "\nExceptions:\n" + data.encounteredExceptions.toString();
        }
            return "\nResults:\n***************************************\n" + displayElements.stream().map(k -> {
                k.executeWithData(data);
                return k.getDisplayReport();
            }).collect(Collectors.joining("\n"));

    }
}
