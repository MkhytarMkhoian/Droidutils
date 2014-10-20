package com.droidutils.http.builder;

import com.droidutils.http.HttpBody;
import com.droidutils.http.HttpHeaders;
import com.droidutils.http.HttpMethod;
import com.droidutils.http.HttpURLConnectionClient;

/**
 * Created by Misha on 07.09.2014.
 */
public class HttpRequest {

    public static final String DEFAULT_KEY = "default_key";

    private HttpHeaders mHttpHeaders;
    private String mUrl;
    private HttpBody mHttpBody;
    private HttpMethod mHttpMethod;
    private int mReadTimeout;
    private int mConnectTimeout;
    private String mRequestKey;

    private HttpRequest(Builder builder) {
        mHttpHeaders = builder.mHttpHeaders;
        mUrl = builder.mUrl;
        mHttpBody = builder.mHttpBody;
        mHttpMethod = builder.mHttpMethod;
        mReadTimeout = builder.mReadTimeout;
        mConnectTimeout = builder.mConnectTimeout;
        mRequestKey = builder.mRequestKey;
    }

    public static class Builder {

        private HttpHeaders mHttpHeaders;
        private String mUrl;
        private HttpBody mHttpBody;
        private HttpMethod mHttpMethod;
        private int mReadTimeout = HttpURLConnectionClient.READ_TIMEOUT;
        private int mConnectTimeout = HttpURLConnectionClient.CONNECT_TIMEOUT;
        private String mRequestKey = DEFAULT_KEY;

        public Builder() {

        }

        public Builder setHttpHeaders(HttpHeaders httpHeaders) {
            this.mHttpHeaders = httpHeaders;
            return this;
        }

        public Builder setUrl(String url) {
            this.mUrl = url;
            return this;
        }

        public Builder setHttpBody(HttpBody httpBody) {
            this.mHttpBody = httpBody;
            return this;
        }

        public Builder setHttpMethod(HttpMethod httpMethod) {
            this.mHttpMethod = httpMethod;
            return this;
        }

        public Builder setReadTimeout(int readTimeout) {
            this.mReadTimeout = readTimeout;
            return this;
        }

        public Builder setConnectTimeout(int connectTimeout) {
            this.mConnectTimeout = connectTimeout;
            return this;
        }

        public Builder setRequestKey(String requestKey) {
            this.mRequestKey = requestKey;
            return this;
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

    public String getRequestKey() {
        return mRequestKey;
    }

    public boolean isHaveBody() {

        if (mHttpBody == null) {
            return false;
        }
        return true;
    }

    public boolean isHaveHeaders() {

        if (mHttpHeaders == null) {
            return false;
        }
        return true;
    }
}
