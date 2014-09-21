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

    private static BackStack sInstance = null;

    private BackStackListener mBackStackListener;
    private Map<String, Integer> mFragmentContainerId;
    private Map<String, ActionType> mFragmentsBackButtonActionType;

    private BackStack() {

        mFragmentContainerId = new HashMap<String, Integer>();
        mFragmentsBackButtonActionType = new HashMap<String, ActionType>();
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

    private int getFragmentBackStackSize(FragmentManager fragmentManager) {
        return fragmentManager.getBackStackEntryCount();
    }


    public <T extends Activity, F extends Fragment> void doAction(Class<T> clazz, FragmentManager fragmentManager, int flag) {

        if (getFragmentBackStackSize(fragmentManager) > 0) {

            Fragment fragment = fragmentManager.findFragmentById(mFragmentContainerId.get(clazz.getName()));

            if (fragment != null) {

                ActionType actionType = mFragmentsBackButtonActionType.get(fragment.getClass().getName());

                if (actionType == ActionType.GO_BACK) {
                    fragmentManager.popBackStack();
                } else {
                    if (mBackStackListener != null) {
                        mBackStackListener.onAction(flag);
                    }
                }
            }
        }
    }

    public void setActionListener(BackStackListener backStackListener) {
        this.mBackStackListener = backStackListener;
    }

    public <T extends Activity> void addFragmentContainerId(Class<T> clazz, int containerId) {

        mFragmentContainerId.put(clazz.getName(), containerId);
    }

    public <T extends Fragment> void setFragmentBackButtonActionType(Class<T> clazz, ActionType actionType) {

        mFragmentsBackButtonActionType.put(clazz.getName(), actionType);
    }

}

