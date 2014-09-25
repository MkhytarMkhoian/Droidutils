package com.droidutils.http;

import com.droidutils.http.builder.HttpRequest;
import com.droidutils.http.builder.HttpResponse;

/**
 * Created by Misha on 08.09.2014.
 */
public class ApacheHttpClient implements HttpConnection {


    @Override
    public <T> HttpResponse get(HttpRequest httpRequest, Class<T> responseType) throws Exception {
        return null;
    }

    @Override
    public <T> HttpResponse post(HttpRequest httpRequest, Class<T> responseType) throws Exception {
        return null;
    }

    @Override
    public <T> HttpResponse put(HttpRequest httpRequest, Class<T> responseType) throws Exception {
        return null;
    }

    @Override
    public <T> HttpResponse head(HttpRequest httpRequest, Class<T> responseType) throws Exception {
        return null;
    }

    @Override
    public <T> HttpResponse delete(HttpRequest httpRequest, Class<T> responseType) throws Exception {
        return null;
    }

    @Override
    public <T> HttpResponse trace(HttpRequest httpRequest, Class<T> responseType) throws Exception {
        return null;
    }

    @Override
    public <T> HttpResponse options(HttpRequest httpRequest, Class<T> responseType) throws Exception {
        return null;
    }
}
