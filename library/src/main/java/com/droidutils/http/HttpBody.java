package com.droidutils.http;

import com.droidutils.jsonparser.JsonConverter;

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

    public Byte[] convertToByteArray(){

        return null;
    }

    public String convertToString(){

        try {
            return new JsonConverter<T>().convertToJsonString(mBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
