package com.droidutils.sample.http;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.droidutils.http.HttpBody;
import com.droidutils.http.HttpMethod;
import com.droidutils.http.builder.HttpRequest;
import com.droidutils.http.cache.Cache;
import com.droidutils.multithreading.ExecutorListener;
import com.droidutils.multithreading.ThreadExecutor;
import com.droidutils.sample.R;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class HttpExampleActivity extends ActionBarActivity implements View.OnClickListener {

    private Button mSendRequestBtn;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_example);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading");

        mSendRequestBtn = (Button) findViewById(R.id.send_request_button);
        mSendRequestBtn.setOnClickListener(this);


    }

    @Override
    protected void onStop() {
        super.onStop();
        ThreadExecutor.shutdownTaskWithInterval();
    }

    @Override
    public void onClick(View v) {
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {

                Thread.sleep(3000);

                return "Hello world";
            }
        };
        ThreadExecutor.doBackgroundTaskAsync(callable, new ExecutorListener<String>() {
            @Override
            public void start() {
                mProgressDialog.show();
            }

            @Override
            public void complete(String result) {
                mProgressDialog.dismiss();
                Toast.makeText(HttpExampleActivity.this, result, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(Exception e) {
                e.printStackTrace();
                mProgressDialog.dismiss();
            }
        });

        ThreadExecutor.doTaskWithInterval(new Runnable() {
            @Override
            public void run() {
                Log.e("doTaskWithInterval", "Hello world With Interval");
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}
