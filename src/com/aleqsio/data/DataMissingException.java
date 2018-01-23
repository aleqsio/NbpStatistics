package com.aleqsio.data;

/**
 * An exception used for signalling situations where some data may not be available, has different types.
 */
public class DataMissingException extends Exception {
    private DataMissingException.dataMissingExceptionType dataMissingExceptionType;
    private String url;
    private String message;

    @Override
    public String toString() {
        return "\nDataMissingException{" +
                "dataMissingExceptionType=" + dataMissingExceptionType +
                ", url='" + url + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public DataMissingException(dataMissingExceptionType dataMissingExceptionType, String url, String message) {

        this.dataMissingExceptionType = dataMissingExceptionType;
        this.url = url;
        this.message = message;
    }

    public enum dataMissingExceptionType {
        NETWORK_ERROR,
        INCORRECT_TIME_RANGE_ERROR, INVALID_REQUEST_ERROR,

    }
}
