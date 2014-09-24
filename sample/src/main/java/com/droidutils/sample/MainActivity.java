package com.droidutils.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.droidutils.backstack.BackStack;
import com.droidutils.jsonparser.JsonConverter;

public class MainActivity extends ActionBarActivity {

    private BackStack mBackStack = BackStack.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JsonConverter converter = new JsonConverter();
        try {
//            String s = converter.convertToJsonString(new Body());
//            Log.e("convert", s);

            String response = "{\n" +
                    "\t\"title\": \"Example Schema\",\n" +
                    "\t\"type\": \"object\",\n" +
                    "\t\"properties\": [\n" +
                    "\t\t{\n" +
                    "\t\t\t\"Name\": \"1\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"Name\": \"2\"\n" +
                    "\t\t}\n" +
                    "\t\t\n" +
                    "\t],\n" +
                    "\t\"required\": [\"firstName\", \"lastName\"]\n" +
                    "}";

            JsonConverter converter2 = new JsonConverter();
            BodyExample bodyExample = converter2.readJson(response, BodyExample.class);
            Log.e("convert", bodyExample.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

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
