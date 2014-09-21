package com.droidutils.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.droidutils.backstack.ActionType;
import com.droidutils.backstack.BackStack;
import com.droidutils.backstack.BackStackListener;

import static com.droidutils.backstack.ActionType.*;

/**
 * Created by Misha on 04.07.2014.
 */
public class Fragment1 extends Fragment implements BackStackListener{

    private BackStack mBackStack = BackStack.getInstance();
    private Button mButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_1, container, false);
        mButton = (Button) v.findViewById(R.id.button1);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManagerUtils.showFragment(Fragment2.class, getFragmentManager(), null, true);
            }
        });


        mBackStack.setActionListener(this);
        return v;

    }

    @Override
    public void onAction(int flag) {
        Toast.makeText(getActivity(), "onAction1", Toast.LENGTH_SHORT).show();
    }
}
