package com.droidutils.test.backstack;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droidutils.backstack.BackButtonListener;
import com.droidutils.backstack.BackStack;
import com.droidutils.test.R;

/**
 * Created by Misha on 04.07.2014.
 */
public class Fragment1 extends Fragment implements BackButtonListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_1, container, false);

    }

    @Override
    public void backButtonPressed(int flag) {

        switch (flag) {

            case BackStack.BACK_BUTTON:

                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setMessage("Are you sure?")
                        .setTitle("Exit")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().finish();
                            }
                        }).setNegativeButton(android.R.string.cancel, null)
                        .create();
                dialog.show();
                break;

        }
    }
}
