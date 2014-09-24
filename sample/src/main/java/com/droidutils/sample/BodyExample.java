package com.droidutils.sample;

import com.droidutils.jsonparser.annotation.JsonKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
    private List<Integer> properties = new ArrayList<Integer>();

    @JsonKey("required")
    private String[] r;

    @Override
    public String toString() {
        return type + " " + title + " " + properties.toString() + " " + Arrays.toString(r);
    }
}
