package com.droidutils.sample.http;

import com.droidutils.jsonparser.annotation.JsonKey;

/**
 * Created by Mkhitar on 16.10.2014.
 */
public class TestResponse {

    @JsonKey("test")
    public String hello;

    @Override
    public String toString() {
        return hello;
    }
}
