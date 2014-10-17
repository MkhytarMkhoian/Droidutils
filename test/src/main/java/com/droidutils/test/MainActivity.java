package com.droidutils.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.droidutils.test.backstack.BackStackExampleActivity;
import com.droidutils.test.http.HttpExampleActivity;
import com.droidutils.test.json.JsonExampleActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    public static final String HTTP = "Http";
    public static final String JSON = "Json";
    public static final String BACK_STACK = "BackStack";
    public static final String TEXT = "text";

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] texts = {HTTP, JSON, BACK_STACK};

        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>(
                texts.length);
        Map<String, Object> m;
        for (int i = 0; i < texts.length; i++) {
            m = new HashMap<String, Object>();
            m.put(TEXT, texts[i]);
            data.add(m);
        }
        String[] from = {TEXT};
        int[] to = {android.R.id.text1};

        SimpleAdapter sAdapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_1,
                from, to);

        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(sAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String text = ((TextView) view).getText().toString();
                Intent intent = null;

                if (text.equals(BACK_STACK)){
                    intent = new Intent(MainActivity.this, BackStackExampleActivity.class);
                } else if (text.equals(HTTP)){
                    intent = new Intent(MainActivity.this, HttpExampleActivity.class);
                } else if (text.equals(JSON)){
                    intent = new Intent(MainActivity.this, JsonExampleActivity.class);
                }
                startActivity(intent);
            }
        });
    }
}
