package com.droidutils.http;

/**
 * Created by Misha on 07.09.2014.
 */
public class HttpHeader implements Header {

    private String mKey;
    private String mValue;

    public HttpHeader(String mKey, String mValue) {
        this.mKey = mKey;
        this.mValue = mValue;
    }

    @Override
    public String getKey() {
        return mKey;
    }

    @Override
    public String getValue() {
        return mValue;
    }

}
