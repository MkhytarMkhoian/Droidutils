package com.droidutils.backstack;

import android.app.FragmentManager;
import android.app.Fragment;

import com.droidutils.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * Created by Misha on 03.07.2014.
 */
public class BackStack {

    public static final int BACK_BUTTON = 0;

    private static BackStack sInstance = null;

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

    public void onBackPressed(FragmentManager fragmentManager, int fragmentContainerId) {

        Fragment fragment = fragmentManager.findFragmentById(fragmentContainerId);

        if (fragment != null && fragment instanceof BackButtonListener){
            ((BackButtonListener) fragment).backButtonPressed(BACK_BUTTON);
        }
    }

//    private Object[] getEmptyArgument(Class<?>[] parameterTypes) throws IllegalAccessException, InstantiationException {
//
//        Object[] objects = new Object[parameterTypes.length];
//        for (int i = 0; i < parameterTypes.length; i++) {
//            if (Number.class.isAssignableFrom(parameterTypes[i])
//                    || parameterTypes[i].isAssignableFrom(int.class)
//                    || parameterTypes[i].isAssignableFrom(double.class)
//                    || parameterTypes[i].isAssignableFrom(float.class)
//                    || parameterTypes[i].isAssignableFrom(short.class)
//                    || parameterTypes[i].isAssignableFrom(byte.class)) {
//                objects[i] = 0;
//            } else if (parameterTypes[i].isAssignableFrom(boolean.class)
//                    || Boolean.class.isAssignableFrom(parameterTypes[i])) {
//                objects[i] = false;
//            } else if (String.class.isAssignableFrom(parameterTypes[i])
//                    || Character.class.isAssignableFrom(parameterTypes[i])
//                    || parameterTypes[i].isAssignableFrom(char.class)) {
//                objects[i] = "";
//            } else if (Collection.class.isAssignableFrom(parameterTypes[i])) {
//                if (parameterTypes[i].isInterface()) {
//                    if (List.class.isAssignableFrom(parameterTypes[i])) {
//                        objects[i] = new ArrayList();
//                    } else if (Set.class.isAssignableFrom(parameterTypes[i])) {
//                        objects[i] = new HashSet();
//                    } else if (Queue.class.isAssignableFrom(parameterTypes[i])) {
//                        objects[i] = new PriorityQueue();
//                    }
//                } else {
//                    objects[i] = (Collection) parameterTypes[i].newInstance();
//                }
//            } else if (Object.class.isAssignableFrom(parameterTypes[i])) {
//                objects[i] = new Object();
//            }
//        }
//        return objects;
//    }
}

