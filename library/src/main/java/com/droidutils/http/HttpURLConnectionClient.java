package com.droidutils.http;

import com.droidutils.http.builder.Request;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    public static final String CHARSET = "UTF-8";

    HttpURLConnection mUrlConnection;

    public HttpURLConnection getHttpURLConnection(Request request) throws Exception{
        URL url = new URL(request.getUrl());
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        mUrlConnection = (HttpURLConnection) url.openConnection();
        mUrlConnection.setReadTimeout(request.getReadTimeout());
        mUrlConnection.setConnectTimeout(request.getConnectTimeout());

        return mUrlConnection;
    }

    private String parseResponse(int status, InputStream in) throws Exception {
        if (status != HttpURLConnection.HTTP_OK) {
            in = mUrlConnection.getErrorStream();
            String error = readStream(in);
            throw new IllegalArgumentException(error);
        } else {
            String response = readStream(in);
            return response;
        }
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
    public String get(Request request) throws Exception {

        try {
            mUrlConnection = getHttpURLConnection(request);

            mUrlConnection.setRequestProperty("Accept-Charset", CHARSET);
            InputStream in = new BufferedInputStream(mUrlConnection.getInputStream());
            int status = mUrlConnection.getResponseCode();
            return parseResponse(status, in);
        } finally {
            mUrlConnection.disconnect();
        }
    }

    @Override
    public String post(Request request) throws Exception {

        try {
            mUrlConnection = getHttpURLConnection(request);

            mUrlConnection.setRequestMethod(HttpMethod.POST.toString());
            mUrlConnection.setUseCaches(false);
            mUrlConnection.setDoOutput(true);
            mUrlConnection.setDoInput(true);
            mUrlConnection.setRequestProperty("Accept-Charset", CHARSET);
            mUrlConnection.setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");

            addHeaders(mUrlConnection, request.getHttpHeaders().getHeaders());

            mUrlConnection.setFixedLengthStreamingMode(request.getHttpBody().getContentLength());

            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(mUrlConnection.getOutputStream()));
            out.writeBytes(request.getHttpBody().convertToString());
            out.flush();
            out.close();

            InputStream in = new BufferedInputStream(mUrlConnection.getInputStream());
            int status = mUrlConnection.getResponseCode();
            return parseResponse(status, in);
        } finally {
            mUrlConnection.disconnect();
        }
    }

    @Override
    public String put(Request request) throws Exception {

        try {
            mUrlConnection = getHttpURLConnection(request);
            mUrlConnection.setRequestMethod(HttpMethod.PUT.toString());
            return null;
        } finally {
            mUrlConnection.disconnect();
        }
    }

    @Override
    public String head(Request request) throws Exception {

        try {
            mUrlConnection = getHttpURLConnection(request);
            mUrlConnection.setRequestMethod(HttpMethod.HEAD.toString());
            return null;
        } finally {
            mUrlConnection.disconnect();
        }

    }

    @Override
    public String delete(Request request) throws Exception {

        try {
            mUrlConnection = getHttpURLConnection(request);
            mUrlConnection.setRequestMethod(HttpMethod.DELETE.toString());
            return null;
        } finally {
            mUrlConnection.disconnect();
        }

    }

    @Override
    public String trace(Request request) throws Exception {

        try {
            mUrlConnection = getHttpURLConnection(request);
            mUrlConnection.setRequestMethod(HttpMethod.TRACE.toString());
            return null;
        } finally {
            mUrlConnection.disconnect();
        }

    }

    @Override
    public String options(Request request) throws Exception {

        try {
            mUrlConnection = getHttpURLConnection(request);
            mUrlConnection.setRequestMethod(HttpMethod.OPTIONS.toString());
            return null;
        } finally {
            mUrlConnection.disconnect();
        }

    }
}
