package com.droidutils.http.builder;

import com.droidutils.jsonparser.JsonConverter;

import java.util.List;
import java.util.Map;

/**
 * Created by Mkhitar on 25.09.2014.
 */
public class HttpResponse<T> {

    private T mResponseBody;
    private Map<String, List<String>> mResponseHeaders;

    public HttpResponse(T responseBody, Map<String, List<String>> responseHeaders) {
        mResponseBody = responseBody;
        mResponseHeaders = responseHeaders;
    }

    public T getBody() throws Exception {

        return mResponseBody;
    }

    public Map<String, List<String>> getHeaders(){
        return mResponseHeaders;
    }

    public String getHeader(String key){

        if (mResponseHeaders.get(key) != null){
            return mResponseHeaders.get(key).get(0);
        }
        return null;
    }
}
