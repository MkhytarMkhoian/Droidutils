package com.droidutils.http;

import com.droidutils.http.builder.HttpRequest;
import com.droidutils.http.builder.HttpResponse;
import com.droidutils.http.cache.Cache;

/**
 * Created by Misha on 08.09.2014.
 */
public class ApacheHttpClient implements HttpConnection {

    @Override
    public <T> HttpResponse<T> get(HttpRequest request, Class<T> responseType, Cache<T> cache) throws Exception {
        return null;
    }

    @Override
    public <T> HttpResponse<T> post(HttpRequest request, Class<T> responseType, Cache<T> cache) throws Exception {
        return null;
    }

    @Override
    public <T> HttpResponse<T> put(HttpRequest request, Class<T> responseType, Cache<T> cache) throws Exception {
        return null;
    }

    @Override
    public <T> HttpResponse<T> head(HttpRequest request, Class<T> responseType, Cache<T> cache) throws Exception {
        return null;
    }

    @Override
    public <T> HttpResponse<T> delete(HttpRequest request, Class<T> responseType, Cache<T> cache) throws Exception {
        return null;
    }

    @Override
    public <T> HttpResponse<T> trace(HttpRequest request, Class<T> responseType, Cache<T> cache) throws Exception {
        return null;
    }

    @Override
    public <T> HttpResponse<T> options(HttpRequest request, Class<T> responseType, Cache<T> cache) throws Exception {
        return null;
    }
}
