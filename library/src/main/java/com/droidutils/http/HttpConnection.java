package com.droidutils.http;

import com.droidutils.http.builder.Request;

import org.json.JSONObject;

/**
 * Created by Misha on 08.09.2014.
 */
public interface HttpConnection {

    public JSONObject get(Request request) throws Exception;
    public JSONObject post(Request request)throws Exception;
    public JSONObject put(Request request)throws Exception;
    public JSONObject head(Request request)throws Exception;
    public JSONObject delete(Request request)throws Exception;
    public JSONObject trace(Request request)throws Exception;
    public JSONObject options(Request request)throws Exception;
}
