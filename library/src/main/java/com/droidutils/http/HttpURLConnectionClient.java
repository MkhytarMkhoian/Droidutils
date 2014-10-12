package com.droidutils.http;

import com.droidutils.http.builder.HttpRequest;
import com.droidutils.http.builder.HttpResponse;
import com.droidutils.jsonparser.JsonConverter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Misha on 07.09.2014.
 */
public class HttpURLConnectionClient implements HttpConnection {

    public static final int READ_TIMEOUT = 20 * 1000;
    public static final int CONNECT_TIMEOUT = 30 * 1000;

    private HttpURLConnection mUrlConnection;
    private RequestManager mRequestManager;
    private JsonConverter mJsonConverter;

    public HttpURLConnectionClient() {
        mRequestManager = new RequestManager();
        mJsonConverter = new JsonConverter();
    }

    public void setRequestLimit(int requestKey, long limit) {
        mRequestManager.setRequestLimit(requestKey, limit);
    }

    public HttpURLConnection getHttpURLConnection(HttpRequest httpRequest) throws Exception {

        if (httpRequest.getUrl() == null) {
            throw new NullPointerException("url is null");
        }
        URL url = new URL(httpRequest.getUrl());
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        mUrlConnection = (HttpURLConnection) url.openConnection();
        mUrlConnection.setReadTimeout(httpRequest.getReadTimeout());
        mUrlConnection.setConnectTimeout(httpRequest.getConnectTimeout());

        return mUrlConnection;
    }

    private <T> HttpResponse parseResponse(Class<T> responseType, InputStream in) throws Exception {
        String response = null;
        int status = mUrlConnection.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            in = mUrlConnection.getErrorStream();
            String error = readStream(in);
            throw new Exception(error);
        } else {
            response = readStream(in);
        }

        T data = mJsonConverter.readJson(response, responseType);

        return new HttpResponse<T>(data, mUrlConnection.getHeaderFields());
    }

    private void addHeaders(HttpURLConnection mUrlConnection, List<HttpHeader> headers) {

        for (HttpHeader header : headers) {
            try {
                mUrlConnection.setRequestProperty(header.getKey(), URLEncoder.encode(header.getValue(), CHARSET));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    @Override
    public <T> HttpResponse get(HttpRequest httpRequest, Class<T> responseType) throws Exception {

        if (!mRequestManager.checkRequestLimit(httpRequest.getRequestKey())) {

            if (httpRequest.getCache() != null) {
                T response = (T) httpRequest.getCache().readFromCache(httpRequest.getRequestKey());
                return new HttpResponse<T>(response, null);
            }

            return new HttpResponse<T>(null, null);
        }

        try {
            mUrlConnection = getHttpURLConnection(httpRequest);
            mUrlConnection.setRequestProperty("Accept-Charset", CHARSET);

            if (httpRequest.isHaveHeaders()) {
                addHeaders(mUrlConnection, httpRequest.getHttpHeaders().getHeaders());
            }
            mUrlConnection.connect();

            InputStream in = new BufferedInputStream(mUrlConnection.getInputStream());
            return parseResponse(responseType, in);
        } finally {
            mUrlConnection.disconnect();
        }
    }

    @Override
    public <T> HttpResponse post(HttpRequest httpRequest, Class<T> responseType) throws Exception {

        if (!mRequestManager.checkRequestLimit(httpRequest.getRequestKey())) {

            if (httpRequest.getCache() != null) {
                T response = (T) httpRequest.getCache().readFromCache(httpRequest.getRequestKey());
                return new HttpResponse<T>(response, null);
            }

            return new HttpResponse<T>(null, null);
        }

        try {
            mUrlConnection = getHttpURLConnection(httpRequest);
            mUrlConnection.setRequestMethod(HttpMethod.POST.toString());
            mUrlConnection.setUseCaches(false);
            mUrlConnection.setDoOutput(true);
            mUrlConnection.setDoInput(true);
            mUrlConnection.setRequestProperty("Accept-Charset", CHARSET);
            mUrlConnection.setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");

            if (httpRequest.isHaveHeaders()) {
                addHeaders(mUrlConnection, httpRequest.getHttpHeaders().getHeaders());
            }

            if (httpRequest.isHaveBody()) {
                mUrlConnection.setFixedLengthStreamingMode(httpRequest.getHttpBody().convertToByteArray().length);
            }

            mUrlConnection.connect();

            if (httpRequest.isHaveBody()) {
                OutputStream out = new DataOutputStream(new BufferedOutputStream(mUrlConnection.getOutputStream()));
                out.write(httpRequest.getHttpBody().convertToByteArray());
                out.flush();
                out.close();
            }

            InputStream in = new BufferedInputStream(mUrlConnection.getInputStream());
            return parseResponse(responseType, in);
        } finally {
            mUrlConnection.disconnect();
        }
    }

    @Override
    public <T> HttpResponse put(HttpRequest httpRequest, Class<T> responseType) throws Exception {

        if (!mRequestManager.checkRequestLimit(httpRequest.getRequestKey())) {

            if (httpRequest.getCache() != null) {
                T response = (T) httpRequest.getCache().readFromCache(httpRequest.getRequestKey());
                return new HttpResponse<T>(response, null);
            }

            return new HttpResponse<T>(null, null);
        }

        try {
            mUrlConnection = getHttpURLConnection(httpRequest);
            mUrlConnection.setRequestProperty("Accept", "application/json");
            mUrlConnection.setRequestProperty("Content-Type", "application/json");
            mUrlConnection.setRequestMethod(HttpMethod.PUT.toString());
            mUrlConnection.setDoOutput(true);

            if (httpRequest.isHaveHeaders()) {
                addHeaders(mUrlConnection, httpRequest.getHttpHeaders().getHeaders());
            }
            mUrlConnection.connect();

            if (httpRequest.isHaveBody()) {
                DataOutputStream out = new DataOutputStream(new BufferedOutputStream(mUrlConnection.getOutputStream()));
                out.writeBytes(httpRequest.getHttpBody().convertToString());
                out.flush();
                out.close();
            }

            InputStream in = new BufferedInputStream(mUrlConnection.getInputStream());
            return parseResponse(responseType, in);
        } finally {
            mUrlConnection.disconnect();
        }
    }

    @Override
    public <T> HttpResponse head(HttpRequest httpRequest, Class<T> responseType) throws Exception {

        try {
            mUrlConnection = getHttpURLConnection(httpRequest);
            mUrlConnection.setRequestMethod(HttpMethod.HEAD.toString());
            mUrlConnection.setDoOutput(true);
            mUrlConnection.setDefaultUseCaches(false);

            if (httpRequest.isHaveHeaders()) {
                addHeaders(mUrlConnection, httpRequest.getHttpHeaders().getHeaders());
            }
            mUrlConnection.connect();

            InputStream in = new BufferedInputStream(mUrlConnection.getInputStream());
            return parseResponse(responseType, in);
        } finally {
            mUrlConnection.disconnect();
        }

    }

    @Override
    public <T> HttpResponse delete(HttpRequest httpRequest, Class<T> responseType) throws Exception {

        try {
            mUrlConnection = getHttpURLConnection(httpRequest);
            mUrlConnection.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded");
            mUrlConnection.setRequestMethod(HttpMethod.DELETE.toString());
            mUrlConnection.setDoOutput(true);

            if (httpRequest.isHaveHeaders()) {
                addHeaders(mUrlConnection, httpRequest.getHttpHeaders().getHeaders());
            }
            mUrlConnection.connect();

            InputStream in = new BufferedInputStream(mUrlConnection.getInputStream());
            return parseResponse(responseType, in);
        } finally {
            mUrlConnection.disconnect();
        }

    }

    @Override
    public <T> HttpResponse trace(HttpRequest httpRequest, Class<T> responseType) throws Exception {

        try {
            mUrlConnection = getHttpURLConnection(httpRequest);
            mUrlConnection.setRequestMethod(HttpMethod.TRACE.toString());

            HttpHeaders headers = httpRequest.getHttpHeaders();
            if (headers != null) {
                addHeaders(mUrlConnection, headers.getHeaders());
            }
            mUrlConnection.connect();

            InputStream in = new BufferedInputStream(mUrlConnection.getInputStream());
            return parseResponse(responseType, in);
        } finally {
            mUrlConnection.disconnect();
        }

    }

    @Override
    public <T> HttpResponse options(HttpRequest httpRequest, Class<T> responseType) throws Exception {

        try {
            mUrlConnection = getHttpURLConnection(httpRequest);
            mUrlConnection.setRequestMethod(HttpMethod.OPTIONS.toString());

            HttpHeaders headers = httpRequest.getHttpHeaders();
            if (headers != null) {
                addHeaders(mUrlConnection, headers.getHeaders());
            }
            mUrlConnection.connect();

            return null;
        } finally {
            mUrlConnection.disconnect();
        }

    }
}
