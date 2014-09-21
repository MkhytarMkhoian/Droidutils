package com.droidutils.jsonparser.analyzer;

/**
 * Created by Mkhitar on 17.09.2014.
 */
public interface Analyzer<T> {

    public T analyzeJson(String json);
    public String analyzeClass(T analyzeObject);
}
