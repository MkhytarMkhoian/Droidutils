package com.droidutils.sample;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Misha on 15.04.2014.
 */
public class FragmentManagerUtils {

    public static <T extends Fragment> Fragment showFragment(Class<T> className, FragmentManager fragmentManager, Bundle data,
                                                             boolean addToBackStack) {

        String name = className.getName();
        Fragment fragment = null;
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        fragment = fragmentManager.findFragmentByTag(name);

        if (fragment != null) {
            transaction.remove(fragment);
        }

        fragment = createNewInstance(name);

        if (data != null) {
            fragment.setArguments(data);
        }

        show(name, fragment, transaction, addToBackStack);

        transaction.commit();

        return fragment;
    }

    private static void show(String tag, Fragment fragment, FragmentTransaction transaction, boolean addToBackStack) {

        transaction.replace(R.id.container, fragment, tag);

        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
    }

    private static Fragment createNewInstance(String className) {

        Fragment fragment = null;
        Class c = null;
        try {
            c = Class.forName(className);
            fragment = (Fragment) c.newInstance();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return fragment;
    }

    public static <T extends Fragment> Fragment showFragment(Class<T> className, FragmentManager fragmentManager, boolean addToBackStack) {

        String name = className.getName();
        Fragment fragment = null;
        fragment = fragmentManager.findFragmentByTag(name);

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (fragment != null) {
            show(name, fragment, transaction, addToBackStack);
        }

        transaction.commit();
        return fragment;
    }

    public static <T extends Fragment> void removeFragment(Class<T> className, FragmentManager fragmentManager) {

        String name = className.getName();
        Fragment fragment = null;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragment = fragmentManager.findFragmentByTag(name);

        if (fragment != null) {
            transaction.remove(fragment);
            transaction.commit();
        }
    }


    public static <T extends DialogFragment> DialogFragment showDialogFragment(Class<T> className,
                                                                               FragmentManager fragmentManager,
                                                                               Bundle data,
                                                                               boolean isCancelable) {

        DialogFragment dialog = null;
        Class c = null;
        try {
            c = Class.forName(className.getName());
            dialog = (DialogFragment) c.newInstance();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (data != null) {
            dialog.setArguments(data);
        }
        dialog.setCancelable(isCancelable);
        dialog.show(fragmentManager, className.getName());

        return dialog;
    }
}
