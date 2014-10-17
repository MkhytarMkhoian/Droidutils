package com.droidutils.sample.json;

import com.droidutils.jsonparser.annotation.JsonKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mkhitar on 21.09.2014.
 */
@JsonKey("BodyClass")
public class Body {

    @JsonKey("key_int")
    private int i = 50;

    @JsonKey("Test")
    private Test test = new Test();

    @JsonKey("double")
    private double aDouble = 5.6161654;

    @JsonKey("array")
    private int[] ints = {12, 516, 65, 44};

    @JsonKey("List")
    private List<Integer> list;

    @JsonKey("Map")
    private Map<String, String> map;

    public Body() {

        list = new ArrayList<Integer>(10);
        map = new HashMap<String, String>(10);
        for (int i = 0; i < 10; i++) {
            list.add(15);
            map.put(i + "", 20 + "");
        }
    }
}
