package com.droidutils.sample.backstack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.droidutils.backstack.BackButtonPressed;
import com.droidutils.backstack.BackStack;
import com.droidutils.sample.R;
import com.droidutils.sample.json.Title;

import java.util.List;

/**
 * Created by Misha on 04.07.2014.
 */
public class Fragment1 extends Fragment {

    private BackStack mBackStack = BackStack.getInstance();
    private Button mButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_1, container, false);
        mButton = (Button) v.findViewById(R.id.button1);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        return v;

    }

    @BackButtonPressed
    public void onBackPressed(Object flag) {

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

    }

    @Override
    public void onResume() {
        super.onResume();
        mBackStack.setFocus(this);
    }

}
