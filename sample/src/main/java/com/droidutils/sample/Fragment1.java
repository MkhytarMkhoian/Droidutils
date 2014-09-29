package com.droidutils.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.droidutils.backstack.BackStack;
import com.droidutils.backstack.GoBackListener;

/**
 * Created by Misha on 04.07.2014.
 */
public class Fragment1 extends Fragment implements GoBackListener {

    private BackStack mBackStack = BackStack.getInstance();
    private Button mButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_1, container, false);
        mButton = (Button) v.findViewById(R.id.button1);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new Fragment2(), Fragment2.class.getName())
                        .addToBackStack(Fragment2.class.getName())
                        .commit();
            }
        });


        return v;

    }

    @Override
    public void onBackPressed(int flag) {

        Toast.makeText(getActivity(), "onAction1 flag" + flag, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        super.onResume();
        mBackStack.captureFocus(this);
    }
}
