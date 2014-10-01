package com.droidutils.sample.json;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.droidutils.jsonparser.JsonConverter;
import com.droidutils.sample.R;

public class JsonExampleActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_example);

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

}
