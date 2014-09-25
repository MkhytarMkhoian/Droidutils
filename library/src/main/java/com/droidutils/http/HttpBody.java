package com.droidutils.http;

import com.droidutils.jsonparser.JsonConverter;

import java.net.URLEncoder;

/**
 * Created by Misha on 07.09.2014.
 */
public class HttpBody<T> {

    private T mBody;

    public HttpBody(T body){
        mBody = body;
    }

    public int getContentLength(){
        return 0;
    }

    public byte[] convertToByteArray() throws Exception{

        return new JsonConverter().convertToJsonString(mBody).getBytes(HttpConnection.CHARSET);
    }

    public String convertToString(){

        try {
            return URLEncoder.encode(new JsonConverter().convertToJsonString(mBody), HttpConnection.CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
