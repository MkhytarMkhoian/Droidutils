package com.droidutils.http;

import com.droidutils.http.builder.HttpRequest;
import com.droidutils.http.builder.HttpResponse;

/**
 * Created by Misha on 08.09.2014.
 */
public interface HttpConnection {

    public static final String CHARSET = "UTF-8";

    public <T> HttpResponse get(HttpRequest httpRequest, Class<T> responseType) throws Exception;
    public <T> HttpResponse post(HttpRequest httpRequest, Class<T> responseType)throws Exception;
    public <T> HttpResponse put(HttpRequest httpRequest, Class<T> responseType)throws Exception;
    public <T> HttpResponse head(HttpRequest httpRequest, Class<T> responseType)throws Exception;
    public <T> HttpResponse delete(HttpRequest httpRequest, Class<T> responseType)throws Exception;
    public <T> HttpResponse trace(HttpRequest httpRequest, Class<T> responseType)throws Exception;
    public <T> HttpResponse options(HttpRequest httpRequest, Class<T> responseType)throws Exception;
}
