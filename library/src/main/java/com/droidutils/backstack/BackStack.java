package com.droidutils.backstack;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Misha on 03.07.2014.
 */
public class BackStack {

    public static final int ACTIVITY_FINISH = 1;
    public static final int GO_BACK = 2;

    private static BackStack sInstance = null;

    private GoBackListener mListener;

    private BackStack() {

    }

    public static BackStack getInstance() {

        if (sInstance == null) {
            sInstance = new BackStack();
        }
        return sInstance;
    }

    public static void release() {

        sInstance = null;
    }

    public void clear(){
        mListener = null;
    }

    public void captureFocus(GoBackListener listener){
        mListener = listener;
    }

    public void goBack(int flag){
        if (mListener != null){
            mListener.onBackPressed(flag);
        }
    }
}

