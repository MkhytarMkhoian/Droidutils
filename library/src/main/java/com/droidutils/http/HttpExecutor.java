package com.droidutils.http;

import com.droidutils.http.builder.HttpRequest;
import com.droidutils.http.builder.HttpResponse;
import com.droidutils.http.cache.Cache;

/**
 * Created by Misha on 08.09.2014.
 */
public class HttpExecutor {

    private HttpConnection mHttpConnection;

    public HttpExecutor(HttpConnection httpConnection) {
        mHttpConnection = httpConnection;
    }

    public <T> HttpResponse execute(HttpRequest httpRequest, Class<T> responseType) throws Exception {

        return executeRequest(httpRequest, responseType, null);
    }

    public <T> HttpResponse execute(HttpRequest httpRequest, Class<T> responseType, Cache<T> cache) throws Exception {

        return executeRequest(httpRequest, responseType, cache);
    }

    private <T> HttpResponse executeRequest(HttpRequest httpRequest, Class<T> responseType, Cache<T> cache) throws Exception {
        HttpResponse result = null;

        switch (httpRequest.getHttpMethod()) {

            case GET:
                result = mHttpConnection.get(httpRequest, responseType, cache);
                break;
            case POST:
                result = mHttpConnection.post(httpRequest, responseType, cache);
                break;
            case HEAD:
                result = mHttpConnection.head(httpRequest, responseType, cache);
                break;
            case OPTIONS:
                result = mHttpConnection.options(httpRequest, responseType, cache);
                break;
            case PUT:
                result = mHttpConnection.put(httpRequest, responseType, cache);
                break;
            case DELETE:
                result = mHttpConnection.delete(httpRequest, responseType, cache);
                break;
            case TRACE:
                result = mHttpConnection.trace(httpRequest, responseType, cache);
                break;
        }
        return result;
    }
}
