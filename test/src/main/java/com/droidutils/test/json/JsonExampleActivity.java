package com.droidutils.test.json;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.droidutils.jsonparser.JsonConverter;
import com.droidutils.test.R;

public class JsonExampleActivity extends ActionBarActivity {

    private TextView mJsonText;
    private TextView mObjectWithJsonData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_example);

        String exampleJson = "{\n" +
                "\t\"title\": \"Example Schema\",\n" +
                "\t\"object\":{\t\"test\": \"ignore\"\n}," +
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

        mJsonText = (TextView) findViewById(R.id.json);
        mJsonText.setText(exampleJson);

        mObjectWithJsonData = (TextView) findViewById(R.id.objectWithJsonData);

        JsonConverter converter = new JsonConverter();
        try {
            String s = converter.convertToJsonString(new Body());
            Log.e("convert", s);

            BodyExample bodyExample = converter.readJson(exampleJson, BodyExample.class);
            mObjectWithJsonData.setText(bodyExample.toString());
            Log.e("convert", bodyExample.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
