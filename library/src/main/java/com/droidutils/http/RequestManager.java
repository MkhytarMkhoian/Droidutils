package com.droidutils.http;

import com.droidutils.http.cache.Cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mkhitar on 12.10.2014.
 */
public class RequestManager {

    private Map<Integer, Long> mRequestLimit;
    private Map<Integer, Long> mRequestTimestamp;

    public RequestManager() {
        mRequestLimit = new HashMap<Integer, Long>();
        mRequestTimestamp = new HashMap<Integer, Long>();
    }

    public void setRequestLimit(int requestKey, long limit) {
        mRequestLimit.put(requestKey, limit);
    }

    public boolean checkRequestLimit(int requestKey) {
        Long limit = mRequestLimit.get(requestKey);
        if (limit == null) {
            resetRequestLimit(requestKey);
            return true;
        }

        Long timestamp = mRequestTimestamp.get(requestKey);
        if (timestamp != null) {
            long currentTimestamp = System.currentTimeMillis();
            if (currentTimestamp < timestamp + limit) {
                return false;
            }
        }

        resetRequestLimit(requestKey);
        return true;
    }

    public void resetRequestLimit(int requestKey) {
        mRequestLimit.put(requestKey, System.currentTimeMillis());
    }
}
