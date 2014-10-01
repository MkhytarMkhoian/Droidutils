package com.droidutils.backstack;

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

    public static final int ACTIVITY_FINISH = 0;
    public static final int GO_BACK = 2;

    private static BackStack sInstance = null;

    private Object mFocus;

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

    public void setFocus(Object focusObject) {
        mFocus = focusObject;
    }

    private void clearFocus() {
       mFocus = null;
    }

    public void onBackPressed() {

        if (mFocus != null) {
            invokeMethod(null);
        }
    }

    private void invokeMethod(Object[] methodArg) {
        Class<?> clazz = mFocus.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            method.setAccessible(true);
            BackButtonPressed annotation = method.getAnnotation(BackButtonPressed.class);

            if (annotation != null) {
                try {
                    Class<?>[] types = method.getParameterTypes();
                    if (types.length > 0) {
                        if (methodArg == null) {
                            methodArg = getEmptyArgument(types);
                        }
                        method.invoke(mFocus, methodArg);
                    } else {
                        method.invoke(mFocus, null);
                    }
                    clearFocus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Object[] getEmptyArgument(Class<?>[] parameterTypes) throws IllegalAccessException, InstantiationException {

        Object[] objects = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            if (Number.class.isAssignableFrom(parameterTypes[i])
                    || parameterTypes[i].isAssignableFrom(int.class)
                    || parameterTypes[i].isAssignableFrom(double.class)
                    || parameterTypes[i].isAssignableFrom(float.class)
                    || parameterTypes[i].isAssignableFrom(short.class)
                    || parameterTypes[i].isAssignableFrom(byte.class)) {
                objects[i] = 0;
            } else if (parameterTypes[i].isAssignableFrom(boolean.class)
                    || Boolean.class.isAssignableFrom(parameterTypes[i])) {
                objects[i] = false;
            } else if (String.class.isAssignableFrom(parameterTypes[i])
                    || Character.class.isAssignableFrom(parameterTypes[i])
                    || parameterTypes[i].isAssignableFrom(char.class)) {
                objects[i] = "";
            } else if (Collection.class.isAssignableFrom(parameterTypes[i])) {
                if (parameterTypes[i].isInterface()) {
                    if (List.class.isAssignableFrom(parameterTypes[i])) {
                        objects[i] = new ArrayList();
                    } else if (Set.class.isAssignableFrom(parameterTypes[i])) {
                        objects[i] = new HashSet();
                    } else if (Queue.class.isAssignableFrom(parameterTypes[i])) {
                        objects[i] = new PriorityQueue();
                    }
                } else {
                    objects[i] = (Collection) parameterTypes[i].newInstance();
                }
            } else if (Object.class.isAssignableFrom(parameterTypes[i])) {
                objects[i] = new Object();
            }
        }
        return objects;
    }
}

