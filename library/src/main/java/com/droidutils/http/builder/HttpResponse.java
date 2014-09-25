package com.droidutils.http.builder;

import com.droidutils.jsonparser.JsonConverter;

import java.util.List;
import java.util.Map;

/**
 * Created by Mkhitar on 25.09.2014.
 */
public class HttpResponse<T> {

    private String mResponseBody;
    private Map<String, List<String>> mResponseHeaders;
    private Class<T> mResponseType;

    public HttpResponse(String responseBody, Map<String, List<String>> responseHeaders, Class<T> responseType) {
        mResponseBody = responseBody;
        mResponseHeaders = responseHeaders;
        mResponseType = responseType;
    }

    public T getBody() throws Exception {

        return new JsonConverter().readJson(mResponseBody, mResponseType);
    }

    public String getBodyToString(){
        return mResponseBody;
    }

    public Map<String, List<String>> getHeaders(){
        return mResponseHeaders;
    }

    public String getHeader(String key){
        return mResponseHeaders.get(key).get(0);
    }
}
