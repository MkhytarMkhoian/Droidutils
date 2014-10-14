package com.droidutils.http;

import com.droidutils.http.builder.HttpRequest;
import com.droidutils.http.builder.HttpResponse;
import com.droidutils.http.cache.Cache;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * Created by Misha on 07.09.2014.
 */
public class HttpURLConnectionClient implements HttpConnection {

    public static final int READ_TIMEOUT = 20 * 1000;
    public static final int CONNECT_TIMEOUT = 30 * 1000;

    private HttpURLConnection mUrlConnection;
    private RequestManager mRequestManager;
    private JsonConverter mJsonConverter;
    private Map<Integer, Semaphore> mRunningRequest;

    public HttpURLConnectionClient() {
        mRequestManager = new RequestManager();
        mJsonConverter = new JsonConverter();
        mRunningRequest = new HashMap<Integer, Semaphore>();
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

    private <T> HttpResponse<T> parseResponse(Class<T> responseType, InputStream in) throws Exception {
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

    private <T> HttpResponse<T> syncWithCache(HttpResponse<T> response, Cache<T> cache, int requestKey) throws Exception {

        if (cache != null) {
            return new HttpResponse<T>(cache.syncCache(response.getBody(), requestKey), response.getHeaders());
        }

        return response;
    }

    private <T> HttpResponse<T> getFromCache(int requestKey, Cache<T> cache) {
        if (cache != null) {
            T response = cache.readFromCache(requestKey);
            return new HttpResponse<T>(response, null);
        }

        return new HttpResponse<T>(null, null);
    }

    private void acquire(int requestKey, Semaphore semaphore) throws InterruptedException {

        if (!mRunningRequest.containsKey(requestKey)) {
            mRunningRequest.put(requestKey, semaphore);
            semaphore.acquire();
        } else {
            mRunningRequest.get(requestKey).acquire();
        }
    }

    private void release(int requestKey) throws InterruptedException {

        if (mRunningRequest.containsKey(requestKey)) {
            mRunningRequest.remove(requestKey).release();
        }
    }

    public <T> HttpResponse get(HttpRequest request, Class<T> responseType) throws Exception {
        return get(request, responseType, null);
    }

    @Override
    public <T> HttpResponse<T> get(HttpRequest request, Class<T> responseType, Cache<T> cache) throws Exception {

        final Semaphore semaphore = new Semaphore(1);
        acquire(request.getRequestKey(), semaphore);

        try {

            if (!mRequestManager.checkRequestLimit(request.getRequestKey())) {

                return getFromCache(request.getRequestKey(), cache);
            }

            mUrlConnection = getHttpURLConnection(request);
            mUrlConnection.setRequestProperty("Accept-Charset", CHARSET);

            if (request.isHaveHeaders()) {
                addHeaders(mUrlConnection, request.getHttpHeaders().getHeaders());
            }
            mUrlConnection.connect();

            InputStream in = new BufferedInputStream(mUrlConnection.getInputStream());

            HttpResponse<T> response = parseResponse(responseType, in);

            return syncWithCache(response, cache, request.getRequestKey());
        } finally {
            mUrlConnection.disconnect();
            release(request.getRequestKey());
        }
    }

    public <T> HttpResponse post(HttpRequest request, Class<T> responseType) throws Exception {
        return post(request, responseType, null);
    }

    @Override
    public <T> HttpResponse<T> post(HttpRequest request, Class<T> responseType, Cache<T> cache) throws Exception {

        final Semaphore semaphore = new Semaphore(1);
        acquire(request.getRequestKey(), semaphore);

        try {

            if (!mRequestManager.checkRequestLimit(request.getRequestKey())) {

                return getFromCache(request.getRequestKey(), cache);
            }

            mUrlConnection = getHttpURLConnection(request);
            mUrlConnection.setRequestMethod(HttpMethod.POST.toString());
            mUrlConnection.setUseCaches(false);
            mUrlConnection.setDoOutput(true);
            mUrlConnection.setDoInput(true);
            mUrlConnection.setRequestProperty("Accept-Charset", CHARSET);
            mUrlConnection.setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");

            if (request.isHaveHeaders()) {
                addHeaders(mUrlConnection, request.getHttpHeaders().getHeaders());
            }

            if (request.isHaveBody()) {
                mUrlConnection.setFixedLengthStreamingMode(request.getHttpBody().convertToByteArray().length);
            }

            mUrlConnection.connect();

            if (request.isHaveBody()) {
                OutputStream out = new DataOutputStream(new BufferedOutputStream(mUrlConnection.getOutputStream()));
                out.write(request.getHttpBody().convertToByteArray());
                out.flush();
                out.close();
            }

            InputStream in = new BufferedInputStream(mUrlConnection.getInputStream());
            HttpResponse<T> response = parseResponse(responseType, in);

            return syncWithCache(response, cache, request.getRequestKey());
        } finally {
            mUrlConnection.disconnect();
            release(request.getRequestKey());
        }
    }

    public <T> HttpResponse put(HttpRequest request, Class<T> responseType) throws Exception {
        return put(request, responseType, null);
    }

    @Override
    public <T> HttpResponse<T> put(HttpRequest request, Class<T> responseType, Cache<T> cache) throws Exception {

        final Semaphore semaphore = new Semaphore(1);
        acquire(request.getRequestKey(), semaphore);

        try {

            if (!mRequestManager.checkRequestLimit(request.getRequestKey())) {
                return getFromCache(request.getRequestKey(), cache);
            }

            mUrlConnection = getHttpURLConnection(request);
            mUrlConnection.setRequestProperty("Accept", "application/json");
            mUrlConnection.setRequestProperty("Content-Type", "application/json");
            mUrlConnection.setRequestMethod(HttpMethod.PUT.toString());
            mUrlConnection.setDoOutput(true);

            if (request.isHaveHeaders()) {
                addHeaders(mUrlConnection, request.getHttpHeaders().getHeaders());
            }
            mUrlConnection.connect();

            if (request.isHaveBody()) {
                DataOutputStream out = new DataOutputStream(new BufferedOutputStream(mUrlConnection.getOutputStream()));
                out.writeBytes(request.getHttpBody().convertToString());
                out.flush();
                out.close();
            }

            InputStream in = new BufferedInputStream(mUrlConnection.getInputStream());
            HttpResponse<T> response = parseResponse(responseType, in);

            return syncWithCache(response, cache, request.getRequestKey());
        } finally {
            mUrlConnection.disconnect();
            release(request.getRequestKey());
        }
    }

    public <T> HttpResponse head(HttpRequest request, Class<T> responseType) throws Exception {
        return head(request, responseType, null);
    }

    @Override
    public <T> HttpResponse<T> head(HttpRequest request, Class<T> responseType, Cache<T> cache) throws Exception {

        final Semaphore semaphore = new Semaphore(1);
        acquire(request.getRequestKey(), semaphore);

        try {
            mUrlConnection = getHttpURLConnection(request);
            mUrlConnection.setRequestMethod(HttpMethod.HEAD.toString());
            mUrlConnection.setDoOutput(true);
            mUrlConnection.setDefaultUseCaches(false);

            if (request.isHaveHeaders()) {
                addHeaders(mUrlConnection, request.getHttpHeaders().getHeaders());
            }
            mUrlConnection.connect();

            InputStream in = new BufferedInputStream(mUrlConnection.getInputStream());
            return parseResponse(responseType, in);
        } finally {
            mUrlConnection.disconnect();
            release(request.getRequestKey());
        }

    }

    public <T> HttpResponse delete(HttpRequest request, Class<T> responseType) throws Exception {
        return delete(request, responseType, null);
    }

    @Override
    public <T> HttpResponse<T> delete(HttpRequest request, Class<T> responseType, Cache<T> cache) throws Exception {

        final Semaphore semaphore = new Semaphore(1);
        acquire(request.getRequestKey(), semaphore);

        try {
            mUrlConnection = getHttpURLConnection(request);
            mUrlConnection.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded");
            mUrlConnection.setRequestMethod(HttpMethod.DELETE.toString());
            mUrlConnection.setDoOutput(true);

            if (request.isHaveHeaders()) {
                addHeaders(mUrlConnection, request.getHttpHeaders().getHeaders());
            }
            mUrlConnection.connect();

            InputStream in = new BufferedInputStream(mUrlConnection.getInputStream());
            return parseResponse(responseType, in);
        } finally {
            mUrlConnection.disconnect();
            release(request.getRequestKey());
        }

    }

    public <T> HttpResponse trace(HttpRequest request, Class<T> responseType) throws Exception {
        return trace(request, responseType, null);
    }

    @Override
    public <T> HttpResponse<T> trace(HttpRequest request, Class<T> responseType, Cache<T> cache) throws Exception {

        final Semaphore semaphore = new Semaphore(1);
        acquire(request.getRequestKey(), semaphore);

        try {
            mUrlConnection = getHttpURLConnection(request);
            mUrlConnection.setRequestMethod(HttpMethod.TRACE.toString());

            HttpHeaders headers = request.getHttpHeaders();
            if (headers != null) {
                addHeaders(mUrlConnection, headers.getHeaders());
            }
            mUrlConnection.connect();

            InputStream in = new BufferedInputStream(mUrlConnection.getInputStream());
            return parseResponse(responseType, in);
        } finally {
            mUrlConnection.disconnect();
            release(request.getRequestKey());
        }

    }

    public <T> HttpResponse options(HttpRequest request, Class<T> responseType) throws Exception {
        return options(request, responseType, null);
    }

    @Override
    public <T> HttpResponse<T> options(HttpRequest request, Class<T> responseType, Cache<T> cache) throws Exception {

        final Semaphore semaphore = new Semaphore(1);
        acquire(request.getRequestKey(), semaphore);

        try {
            mUrlConnection = getHttpURLConnection(request);
            mUrlConnection.setRequestMethod(HttpMethod.OPTIONS.toString());

            HttpHeaders headers = request.getHttpHeaders();
            if (headers != null) {
                addHeaders(mUrlConnection, headers.getHeaders());
            }
            mUrlConnection.connect();

            return null;
        } finally {
            mUrlConnection.disconnect();
            release(request.getRequestKey());
        }

    }
}
