package com.aleqsio;

import com.aleqsio.asyncQuerying.AsyncSingleQueryServant;
import com.aleqsio.data.*;
import com.aleqsio.data.deserialiser.CurrencyGsonDeserialiserFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.aleqsio.display.elements.*;
import com.aleqsio.display.DataDisplay;
import org.apache.commons.cli.*;

public class Main {

    public static void main(String[] args) {
        CatchedDataService catchedDataService = new CatchedDataService(new AsyncSingleQueryServant(new CurrencyGsonDeserialiserFactory()));
        DataDisplay dataDisplay = new DataDisplay();
        CommandLine cmd = parseCmd(args,false);
        assert cmd != null;


        if (cmd.hasOption("priceAtDate") && hasAllReuquiredParams(cmd, "date", "currencyCode")) {
            String exchangeType = cmd.hasOption("exchangeType") ? cmd.getOptionValue("exchangeType").toUpperCase() : "AVERAGE";
            CurrencyData.CurrencyDateTypeKey currencyDateTypeKey = new CurrencyData.CurrencyDateTypeKey(LocalDate.parse(cmd.getOptionValue("date")), cmd.getOptionValue("currencyCode"), CurrencyData.ExchangeType.fromParam(exchangeType));
            catchedDataService.requestValueOfCurrency(currencyDateTypeKey);
            dataDisplay.addElement(new CurrencyKeyDisplayElement(currencyDateTypeKey));
        } else if (cmd.hasOption("averagePriceOfRange") && hasAllReuquiredParams(cmd, "startDate", "endDate")) {
            String currencyCode = cmd.hasOption("currencyCode") ? cmd.getOptionValue("currencyCode") : "GOLD";
            String exchangeType = cmd.hasOption("exchangeType") ? cmd.getOptionValue("exchangeType").toUpperCase() : "AVERAGE";
            catchedDataService.requestTimeRangeOfCurrency(LocalDate.parse(cmd.getOptionValue("startDate")), LocalDate.parse(cmd.getOptionValue("endDate")), CurrencyData.ExchangeType.fromParam(exchangeType), currencyCode);
            dataDisplay.addElement(new CurrencyRangeAveragePriceDisplayElement(LocalDate.parse(cmd.getOptionValue("startDate")), LocalDate.parse(cmd.getOptionValue("endDate")), CurrencyData.ExchangeType.fromParam(exchangeType), currencyCode));
        } else if (cmd.hasOption("largestVariation") && hasAllReuquiredParams(cmd, "startDate")) {
            String exchangeType = cmd.hasOption("exchangeType") ? cmd.getOptionValue("exchangeType").toUpperCase() : "AVERAGE";
            LocalDate endDate = cmd.hasOption("endDate") ? LocalDate.parse(cmd.getOptionValue("endDate")) : LocalDateTime.now().toLocalDate();
            catchedDataService.requestTimeRangeOfAllCurrencies(LocalDate.parse(cmd.getOptionValue("startDate")), endDate, CurrencyData.ExchangeType.fromParam(exchangeType));
            dataDisplay.addElement(new VariationDisplayElement(LocalDate.parse(cmd.getOptionValue("startDate")), endDate, CurrencyData.ExchangeType.fromParam(exchangeType)));
        } else if (cmd.hasOption("cheapestCurrencyAtDate") && hasAllReuquiredParams(cmd, "date")) {
            LocalDate date = LocalDate.parse(cmd.getOptionValue("date"));
            catchedDataService.requestTimeRangeOfAllCurrencies(date, date, CurrencyData.ExchangeType.BUY);
            dataDisplay.addElement(new CheapestCurrencyElement(date));
        } else if (cmd.hasOption("sortByDifference") && hasAllReuquiredParams(cmd, "date")) {
            LocalDate date = LocalDate.parse(cmd.getOptionValue("date"));
            catchedDataService.requestTimeRangeOfAllCurrencies(date, date, CurrencyData.ExchangeType.BUY);
            catchedDataService.requestTimeRangeOfAllCurrencies(date, date, CurrencyData.ExchangeType.SELL);
            dataDisplay.addElement(new SortElement(date));
        } else if (cmd.hasOption("extremesOfCurrency") && hasAllReuquiredParams(cmd, "currencyCode")) {
            catchedDataService.requestTimeRangeOfCurrency(LocalDate.of(2002, 01, 02), LocalDate.now(), CurrencyData.ExchangeType.AVERAGE, cmd.getOptionValue("currencyCode"));
            dataDisplay.addElement(new MaxMinElement(cmd.getOptionValue("currencyCode")));
        } else if (cmd.hasOption("graphData") && hasAllReuquiredParams(cmd, "startDate", "endDate","currencyCode")) {
            catchedDataService.requestTimeRangeOfCurrency(LocalDate.parse(cmd.getOptionValue("startDate")), LocalDate.parse(cmd.getOptionValue("endDate")), CurrencyData.ExchangeType.AVERAGE, cmd.getOptionValue("currencyCode"));
            dataDisplay.addElement(new GraphElement(LocalDate.parse(cmd.getOptionValue("startDate")), LocalDate.parse(cmd.getOptionValue("endDate")), cmd.getOptionValue("currencyCode")));
        }
        else {
          parseCmd(null,true);
            System.exit(1);
        }

        try {
            CurrencyData currencyData = catchedDataService.get();
            System.out.println(dataDisplay.getDisplayOfData(currencyData));
        } catch (DataMissingException e) {
            e.printStackTrace();
        }

    }

    private static boolean hasAllReuquiredParams(CommandLine cmd, String... opts) {
        boolean isOk = true;
        for (String opt : opts) {
            if (!cmd.hasOption(opt)) {
                System.out.println("Required option missing: " + opt);
                isOk = false;
            }
        }
        return isOk;
    }

    private static CommandLine parseCmd(String[] args, boolean displayOptions) {
        //modes
        Options options = new Options();
        Option input = new Option("p", "priceAtDate", false, "display price of gold (GOLD) or any currency by code at any given date");
        options.addOption(input);
        input = new Option("a", "averagePriceOfRange", false, "display average price of gold (GOLD) or any currency by code  in a given time period");
        options.addOption(input);
        input = new Option("v", "largestVariation", false, "find a currency with the highest max/min difference in a given time period");
        options.addOption(input);
        input = new Option("f", "cheapestCurrencyAtDate", false, "find a currency that was the cheapest at a given date");
        options.addOption(input);
        input = new Option("o", "sortByDifference", false, "list all currencies by the sell/buy difference at a given date");
        options.addOption(input);
        input = new Option("x", "extremesOfCurrency", false, "get the smallest and largest price of currency in the entire api history");
        options.addOption(input);
        input = new Option("g", "graphData", false, "draws a chart of data in a given time period");
        options.addOption(input);
        //params
        input = new Option("d", "date", true, "date for single date operations");
        input.setArgName("date");
        options.addOption(input);
        input = new Option("s", "startDate", true, "start date for range operations");
        input.setArgName("startDate");
        options.addOption(input);
        input = new Option("e", "endDate", true, "end date for range operations");
        input.setArgName("endDate");
        options.addOption(input);
        input = new Option("c", "currencyCode", true, "currency code or GOLD for gold");
        input.setArgName("currencyCode");
        options.addOption(input);
        input = new Option("t", "exchangeType", true, "exchangeType, SELL, BUY or AVERAGE, AVERAGE if not any of the provided values");
        input.setArgName("exchangeType");
        options.addOption(input);
        CommandLineParser parser = new DefaultParser();
        if (displayOptions) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.setOptionComparator(null);
            formatter.printHelp("nbpStatistics", options);
            return null;
        } else {
            try {
                return parser.parse(options, args);
            } catch (org.apache.commons.cli.ParseException e) {
                System.out.println(e.getMessage());
                HelpFormatter formatter = new HelpFormatter();
                formatter.setOptionComparator(null);
                formatter.printHelp("nbpStatistics", options);
                System.exit(1);
                return null;
            }

        }
    }
}
