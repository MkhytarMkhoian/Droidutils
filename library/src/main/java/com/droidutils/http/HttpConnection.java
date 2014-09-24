package com.droidutils.http;

import com.droidutils.http.builder.Request;

import org.json.JSONObject;

/**
 * Created by Misha on 08.09.2014.
 */
public interface HttpConnection {

    public String get(Request request) throws Exception;
    public String post(Request request)throws Exception;
    public String put(Request request)throws Exception;
    public String head(Request request)throws Exception;
    public String delete(Request request)throws Exception;
    public String trace(Request request)throws Exception;
    public String options(Request request)throws Exception;
}
