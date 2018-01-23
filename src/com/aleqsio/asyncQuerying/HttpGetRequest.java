package com.aleqsio.asyncQuerying;

import com.aleqsio.data.DataMissingException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

public class HttpGetRequest {
    public final static int TIMEOUT=5000;
    String url;
    public HttpGetRequest(String url) {
        this.url=url;
    }

    /**
     * @return Downloads data from url, unless it failes or exceeds timeout.
     * @throws DataMissingException
     */
    public String getDataAsString() throws DataMissingException {
        HttpURLConnection connection = null;
        final int responseCode;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(TIMEOUT);
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            throw new DataMissingException(DataMissingException.dataMissingExceptionType.NETWORK_ERROR, url, "failed to connect to host");
        }
        switch (responseCode) {
            case 404:
                throw new DataMissingException(DataMissingException.dataMissingExceptionType.INCORRECT_TIME_RANGE_ERROR, url, "404 error");
            case 400:
                throw new DataMissingException(DataMissingException.dataMissingExceptionType.INVALID_REQUEST_ERROR ,url,"400 error");
            case 200: {
                try {
                    return new Scanner(connection.getInputStream(), "UTF-8").useDelimiter("\\A").next();
                } catch (IOException e) {
                    throw new DataMissingException(DataMissingException.dataMissingExceptionType.NETWORK_ERROR,url,"200 error");
                }
            }
            default:
                return null;
        }
    }
}
