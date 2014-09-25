package com.droidutils.http;

import com.droidutils.http.builder.HttpRequest;
import com.droidutils.http.builder.HttpResponse;

/**
 * Created by Misha on 08.09.2014.
 */
public class HttpExecutor {

    private HttpConnection mHttpConnection;

    public HttpExecutor(HttpConnection httpConnection) {
        mHttpConnection = httpConnection;
    }

    public <T> HttpResponse execute(HttpRequest httpRequest, Class<T> responseType) throws Exception {

        HttpResponse result = null;

        switch (httpRequest.getHttpMethod()) {

            case GET:
                result = mHttpConnection.get(httpRequest, responseType);
                break;
            case POST:
                result = mHttpConnection.post(httpRequest, responseType);
                break;
            case HEAD:
                result = mHttpConnection.head(httpRequest, responseType);
                break;
            case OPTIONS:
                result = mHttpConnection.options(httpRequest, responseType);
                break;
            case PUT:
                result = mHttpConnection.put(httpRequest, responseType);
                break;
            case DELETE:
                result = mHttpConnection.delete(httpRequest, responseType);
                break;
            case TRACE:
                result = mHttpConnection.trace(httpRequest, responseType);
                break;
        }
        return result;
    }
}
