package com.droidutils.jsonparser.analyzer;

/**
 * Created by Misha on 07.09.2014.
 */
public class AnnotationAnalyzer<T> implements Analyzer<T> {

    @Override
    public T analyzeJson(String json) {
        return null;
    }

    @Override
    public String analyzeClass(T analyzeObject) {
        return null;
    }
}
