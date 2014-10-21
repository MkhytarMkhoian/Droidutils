package com.droidutils.test.http;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.droidutils.http.HttpExecutor;
import com.droidutils.http.HttpMethod;
import com.droidutils.http.HttpURLConnectionClient;
import com.droidutils.http.builder.HttpRequest;
import com.droidutils.http.builder.HttpResponse;
import com.droidutils.http.builder.Url;
import com.droidutils.http.cache.Cache;
import com.droidutils.multithreading.ExecutorListener;
import com.droidutils.multithreading.ThreadExecutor;
import com.droidutils.test.R;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HttpExampleActivity extends Activity implements View.OnClickListener {

    public static final String REQUEST_ONE = "REQUEST_ONE";
    public static final String REQUEST_TWO = "REQUEST_TWO";

    private ProgressDialog mProgressDialog;
    private HttpExecutor mHttpExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_example);

        HttpURLConnectionClient mHttpURLConnectionClient = new HttpURLConnectionClient();
        mHttpURLConnectionClient.setRequestLimit(REQUEST_ONE, 30000);
        mHttpURLConnectionClient.setRequestLimit(REQUEST_TWO, 30000);
        mHttpExecutor = new HttpExecutor(mHttpURLConnectionClient);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading");

        Button testMultiThreadingBtn = (Button) findViewById(R.id.test_multi_threading_button);
        Button usedCacheBtn = (Button) findViewById(R.id.used_cache_button);
        Button usedRequestWithLimitBtn = (Button) findViewById(R.id.used_request_with_limit_button);

        testMultiThreadingBtn.setOnClickListener(this);
        usedCacheBtn.setOnClickListener(this);
        usedRequestWithLimitBtn.setOnClickListener(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        ThreadExecutor.shutdownTaskWithInterval();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.test_multi_threading_button:
                testMultiThreading();
                break;
            case R.id.used_cache_button:
                testCache();
                break;
            case R.id.used_request_with_limit_button:
                testRequestLimit();
                break;
        }

    }

    private void testRequestLimit() {

        for (int i = 0; i < 2; i++) {

            ThreadExecutor.doNetworkTaskAsync(new Callable<HttpResponse>() {
                @Override
                public HttpResponse call() throws Exception {

                    String url = new Url.Builder("http://10.0.3.2").build();

                    HttpRequest request = new HttpRequest.Builder()
                            .setRequestKey(REQUEST_TWO)
                            .setHttpMethod(HttpMethod.GET)
                            .setUrl(url)
//                            .setHttpBody()
//                            .setHttpHeaders()
//                            .setReadTimeout()
//                            .setConnectTimeout()
                            .build();

                    return mHttpExecutor.execute(request, TestResponse.class);

                }
            }, new ExecutorListener<HttpResponse>() {
                @Override
                public void start() {
                    mProgressDialog.show();
                }

                @Override
                public void complete(HttpResponse result) {
                    mProgressDialog.dismiss();
                    Log.e("REQUEST_TWO", result.toString());
                }

                @Override
                public void error(Exception e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                }
            });
        }
    }

    private void testCache() {

        for (int i = 0; i < 2; i++) {

            ThreadExecutor.doNetworkTaskAsync(new Callable<HttpResponse>() {
                @Override
                public HttpResponse call() throws Exception {

                    String url = new Url.Builder("http://10.0.3.2").build();

                    HttpRequest request = new HttpRequest.Builder()
                            .setRequestKey(REQUEST_ONE)
                            .setHttpMethod(HttpMethod.GET)
                            .setUrl(url).build();

                    return mHttpExecutor.execute(request, TestResponse.class, new Cache<TestResponse>() {
                        @Override
                        public TestResponse syncCache(TestResponse data, String requestKey) {
                            // write data to cache and return this data from cache
                            return data;
                        }

                        @Override
                        public TestResponse readFromCache(String requestKey) {
                            TestResponse response = new TestResponse();
                            response.hello = "hello from cache";
                            return response;
                        }
                    });
                }
            }, new ExecutorListener<HttpResponse>() {
                @Override
                public void start() {
                    mProgressDialog.show();
                }

                @Override
                public void complete(HttpResponse result) {
                    mProgressDialog.dismiss();
                    Log.e("REQUEST_ONE", result.toString());
                }

                @Override
                public void error(Exception e) {
                    mProgressDialog.dismiss();
                }
            });
        }
    }

    private void testMultiThreading() {
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

        ScheduledFuture<?> scheduledFuture = ThreadExecutor.doTaskWithInterval(new Runnable() {
            @Override
            public void run() {
                Log.e("doTaskWithInterval", "Hello world With Interval");
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}
