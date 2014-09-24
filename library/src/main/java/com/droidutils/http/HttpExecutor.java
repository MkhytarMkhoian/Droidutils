package com.droidutils.http;

import com.droidutils.http.builder.Request;

import org.json.JSONObject;

/**
 * Created by Misha on 08.09.2014.
 */
public class HttpExecutor {

    private HttpConnection mHttpConnection;

    public HttpExecutor(HttpConnection httpConnection) {
        mHttpConnection = httpConnection;
    }

    public String execute(Request request) throws Exception {

        String result = null;

        switch (request.getHttpMethod()) {

            case GET:
                result = mHttpConnection.get(request);
                break;
            case POST:
                result = mHttpConnection.post(request);
                break;
            case HEAD:
                result = mHttpConnection.head(request);
                break;
            case OPTIONS:
                result = mHttpConnection.options(request);
                break;
            case PUT:
                result = mHttpConnection.put(request);
                break;
            case DELETE:
                result = mHttpConnection.delete(request);
                break;
            case TRACE:
                result = mHttpConnection.trace(request);
                break;
        }
        return result;
    }
}
