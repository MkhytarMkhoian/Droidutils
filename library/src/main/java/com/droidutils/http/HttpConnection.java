package com.droidutils.http;

import com.droidutils.http.builder.HttpRequest;
import com.droidutils.http.builder.HttpResponse;
import com.droidutils.http.cache.Cache;

/**
 * Created by Misha on 08.09.2014.
 */
public interface HttpConnection {

    public static final String CHARSET = "UTF-8";

    public <T> HttpResponse<T> get(HttpRequest request, Class<T> responseType, Cache<T> cache) throws Exception;

    public <T> HttpResponse<T> post(HttpRequest request, Class<T> responseType, Cache<T> cache) throws Exception;

    public <T> HttpResponse<T> put(HttpRequest request, Class<T> responseType, Cache<T> cache) throws Exception;

    public <T> HttpResponse<T> head(HttpRequest request, Class<T> responseType, Cache<T> cache) throws Exception;

    public <T> HttpResponse<T> delete(HttpRequest request, Class<T> responseType, Cache<T> cache) throws Exception;

    public <T> HttpResponse<T> trace(HttpRequest request, Class<T> responseType, Cache<T> cache) throws Exception;

    public <T> HttpResponse<T> options(HttpRequest request, Class<T> responseType, Cache<T> cache) throws Exception;
}
