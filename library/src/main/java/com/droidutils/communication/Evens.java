package com.droidutils.communication;

/**
 * Created by Mkhitar on 18.10.2014.
 */
public class Evens {

    private static final Evens sInstance = null;

    private Evens(){

    }

    public static Evens getInstance(){

        if (sInstance == null){
            return new Evens();
        }
        return sInstance;
    }
}
