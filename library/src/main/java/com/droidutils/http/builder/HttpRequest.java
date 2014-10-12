package com.droidutils.http.builder;

import com.droidutils.http.HttpBody;
import com.droidutils.http.HttpHeaders;
import com.droidutils.http.HttpMethod;
import com.droidutils.http.cache.Cache;

/**
 * Created by Misha on 07.09.2014.
 */
public class HttpRequest<T> {

    private HttpHeaders mHttpHeaders;
    private String mUrl;
    private HttpBody mHttpBody;
    private HttpMethod mHttpMethod;
    private int mReadTimeout;
    private int mConnectTimeout;
    private int mRequestKey;
    private Cache<T> mCache;

    public HttpRequest() {

    }

    public void setHttpHeaders(HttpHeaders httpHeaders) {
        this.mHttpHeaders = httpHeaders;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public void setHttpBody(HttpBody httpBody) {
        this.mHttpBody = httpBody;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.mHttpMethod = httpMethod;
    }

    public void setReadTimeout(int readTimeout) {
        this.mReadTimeout = readTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.mConnectTimeout = connectTimeout;

    }

    public void setRequestKey(int requestKey) {
        this.mRequestKey = requestKey;
    }

    public void setCache(Cache<T> cache) {
        this.mCache = cache;

    }

    public HttpHeaders getHttpHeaders() {
        return mHttpHeaders;
    }

    public String getUrl() {
        return mUrl;
    }

    public HttpBody getHttpBody() {
        return mHttpBody;
    }

    public HttpMethod getHttpMethod() {
        return mHttpMethod;
    }

    public int getReadTimeout() {
        return mReadTimeout;
    }

    public int getConnectTimeout() {
        return mConnectTimeout;
    }

    public int getRequestKey() {
        return mRequestKey;
    }

    public Cache<T> getCache() {
        return mCache;
    }

    public boolean isHaveBody() {

        return mHttpBody != null;
    }

    public boolean isHaveHeaders() {

        return mHttpHeaders != null;
    }
}
