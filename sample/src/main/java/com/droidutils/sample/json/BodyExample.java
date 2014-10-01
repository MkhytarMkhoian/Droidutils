package com.droidutils.sample.json;

import com.droidutils.jsonparser.annotation.JsonKey;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mkhitar on 22.09.2014.
 */
public class BodyExample {

    @JsonKey("type")
    private String type;
    @JsonKey("title")
    private String title;

    @JsonKey("properties")
    private LinkedList<Title> properties;

    @JsonKey("required")
    private List<String> r;

    @Override
    public String toString() {
        return type + " " + title + " " + properties.toString() + " " + r.toString();
    }
}
