package com.droidutils.jsonparser;

import com.droidutils.jsonparser.analyzer.Analyzer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Misha on 11.09.2014.
 */
public interface AbstractJsonConverter<T> {

    public T readJson(JSONObject jsonObject) throws JSONException;
    public String convertToJsonString(T object) throws JSONException, Exception;
}
