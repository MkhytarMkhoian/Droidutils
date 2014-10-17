package com.droidutils.test.backstack;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.droidutils.backstack.BackStack;
import com.droidutils.test.R;

public class BackStackExampleActivity extends ActionBarActivity {

    private BackStack mBackStack = BackStack.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_stack);

        getFragmentManager().beginTransaction().replace(R.id.container, new Fragment1(), Fragment1.class.getName())
                .addToBackStack(Fragment1.class.getName())
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BackStack.release();
    }

    @Override
    public void onBackPressed() {
        mBackStack.onBackPressed(getFragmentManager(), R.id.container);
    }


}
