package com.droidutils.http;

import com.droidutils.jsonparser.JsonConverter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Misha on 07.09.2014.
 */
public class HttpBody<T> {

    private T mBody;
    private String mJsonBody;
    private JsonConverter mJsonConverter;

    public HttpBody(T body) throws Exception {
        mBody = body;
        mJsonConverter = new JsonConverter();
        mJsonBody = mJsonConverter.convertToJsonString(mBody);
    }

    public byte[] convertToByteArray() {

        try {
            return mJsonBody.getBytes(HttpConnection.CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String convertToString(){

        try {
            return URLEncoder.encode(mJsonBody, HttpConnection.CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public T getBody(){
        return mBody;
    }
}
