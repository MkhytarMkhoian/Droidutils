package com.droidutils.test.json;

import com.droidutils.jsonparser.annotation.JsonKey;

/**
 * Created by Mkhitar on 10.10.2014.
 */
public class Ignore {

    @JsonKey("test")
    private String ignore;

    @Override
    public String toString() {
        return ignore;
    }
}
