package com.droidutils.http.builder;

import com.droidutils.http.HttpBody;
import com.droidutils.http.HttpHeaders;
import com.droidutils.http.HttpURLConnectionClient;
import com.droidutils.http.HttpMethod;

/**
 * Created by Misha on 07.09.2014.
 */
public class HttpRequest {

    private HttpHeaders mHttpHeaders;
    private String mUrl;
    private HttpBody mHttpBody;
    private HttpMethod mHttpMethod;
    private int mReadTimeout;
    private int mConnectTimeout;

    private HttpRequest(Builder builder) {
        mHttpHeaders = builder.mHttpHeaders;
        mUrl = builder.mUrl;
        mHttpBody = builder.mHttpBody;
        mHttpMethod = builder.mHttpMethod;
        mReadTimeout = builder.mReadTimeout;
        mConnectTimeout = builder.mConnectTimeout;
    }

    public static class Builder<T> {

        private HttpHeaders mHttpHeaders;
        private String mUrl;
        private HttpBody mHttpBody;
        private HttpMethod mHttpMethod;
        private int mReadTimeout = HttpURLConnectionClient.READ_TIMEOUT;
        private int mConnectTimeout = HttpURLConnectionClient.CONNECT_TIMEOUT;

        public Builder() {

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

        public HttpRequest build() {
            return new HttpRequest(this);
        }
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

    public boolean isHaveBody(){

        if (mHttpBody == null){
            return false;
        }
        return true;
    }

    public boolean isHaveHeaders(){

        if (mHttpHeaders == null){
            return false;
        }
        return true;
    }
}
