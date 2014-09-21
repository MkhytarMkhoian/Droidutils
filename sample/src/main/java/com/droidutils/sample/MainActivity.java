package com.droidutils.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.droidutils.backstack.BackStack;

import static com.droidutils.backstack.ActionType.DO_SOMETHING;
import static com.droidutils.backstack.ActionType.GO_BACK;

public class MainActivity extends ActionBarActivity {

    private BackStack mBackStack = BackStack.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBackStack.addFragmentContainerId(MainActivity.class, R.id.container);
        mBackStack.setFragmentBackButtonActionType(Fragment2.class, GO_BACK);
        mBackStack.setFragmentBackButtonActionType(Fragment1.class, DO_SOMETHING);

        FragmentManagerUtils.showFragment(Fragment1.class, getSupportFragmentManager(), null, true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BackStack.release();
    }

    @Override
    public void onBackPressed() {

    }
}
