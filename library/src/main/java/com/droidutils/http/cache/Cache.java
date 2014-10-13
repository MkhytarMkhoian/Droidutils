package com.droidutils.http.cache;

/**
 * Created by Mkhitar on 12.10.2014.
 */
public interface Cache<T> {

    public T syncCache(T data, int requestKey);
    public T readFromCache(int requestKey);
}
