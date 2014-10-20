package com.droidutils.http;

import com.droidutils.http.cache.Cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mkhitar on 12.10.2014.
 */
public class RequestManager {

    private Map<String, Long> mRequestLimit;
    private Map<String, Long> mRequestTimestamp;

    public RequestManager() {
        mRequestLimit = new HashMap<String, Long>();
        mRequestTimestamp = new HashMap<String, Long>();
    }

    public void setRequestLimit(String requestKey, long limit) {
        mRequestLimit.put(requestKey, limit);
    }

    public boolean checkRequestLimit(String requestKey) {
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

    public void resetRequestLimit(String requestKey) {
        mRequestTimestamp.put(requestKey, System.currentTimeMillis());
    }
}
