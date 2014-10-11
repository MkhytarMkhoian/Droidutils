package com.droidutils.http;

import com.droidutils.jsonparser.JsonConverter;

import java.net.URLEncoder;

/**
 * Created by Misha on 07.09.2014.
 */
public class HttpBody<T> {

    private T mBody;
    private JsonConverter mJsonConverter;

    public HttpBody(T body){
        mBody = body;
        mJsonConverter = new JsonConverter();
    }

    public byte[] convertToByteArray() throws Exception {

        return mJsonConverter.convertToJsonString(mBody).getBytes(HttpConnection.CHARSET);
    }

    public String convertToString(){

        try {
            return URLEncoder.encode(mJsonConverter.convertToJsonString(mBody), HttpConnection.CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
