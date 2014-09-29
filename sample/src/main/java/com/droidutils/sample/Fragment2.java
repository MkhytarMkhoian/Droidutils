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
public class Fragment2 extends Fragment implements GoBackListener {

    private BackStack mBackStack = BackStack.getInstance();
    private Button mButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_2, container, false);
        mButton = (Button) v.findViewById(R.id.button2);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackStack.goBack(BackStack.GO_BACK);
            }
        });

        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        mBackStack.captureFocus(this);
    }

    @Override
    public void onBackPressed(int flag) {

        switch (flag){
            case BackStack.GO_BACK:
                Toast.makeText(getActivity(), "onAction2 flag" + flag, Toast.LENGTH_SHORT).show();
                break;
            case BackStack.ACTIVITY_FINISH:
                getFragmentManager().popBackStack();
                break;
        }

    }
}
