package com.droidutils.sample.json;

import com.droidutils.jsonparser.annotation.JsonKey;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by Mkhitar on 22.09.2014.
 */
public class BodyExample {

    @JsonKey("test")
    private String test;

    @JsonKey("type")
    private String type;
    @JsonKey("title")
    private String title;

    @JsonKey("properties")
    private LinkedList<Title> properties;

    @JsonKey("required")
    private String[] required;

    @Override
    public String toString() {
        return "test : " + test + " \n" + "type : " + type + " \n" + "title : " + title + " \n" + "properties : " + properties.toString() + " \n" + "required : " + Arrays.toString(required);
    }
}
