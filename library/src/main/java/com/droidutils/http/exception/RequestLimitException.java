package com.droidutils.http.exception;

/**
 * Created by Mkhitar on 18.10.2014.
 */
public class RequestLimitException extends Exception {


    public RequestLimitException() {
    }

    public RequestLimitException(String detailMessage) {
        super(detailMessage);
    }


    public RequestLimitException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }


    public RequestLimitException(Throwable throwable) {
        super(throwable);
    }

}
