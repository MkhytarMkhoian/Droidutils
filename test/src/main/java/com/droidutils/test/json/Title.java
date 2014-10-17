package com.droidutils.test.json;

import com.droidutils.jsonparser.annotation.JsonKey;

/**
 * Created by Mkhitar on 23.09.2014.
 */
public class Title {

    @JsonKey("Name")
    private String name;

    @Override
    public String toString() {
        return "Name : " + name;
    }
}
