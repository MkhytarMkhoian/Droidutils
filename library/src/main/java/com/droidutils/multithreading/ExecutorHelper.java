package com.droidutils.multithreading;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Misha on 06.09.2014.
 */
public class ExecutorHelper {

    public static final int DEFAULT_THREADS = 10;
    public static final int DEFAULT_SCHEDULED_THREADS = 3;
    public static final int DEFAULT_REQUEST_TIMEOUT = 60;

    private static ExecutorService mThreadPoolExecutor = Executors.newCachedThreadPool();
    private static ExecutorService mNetworkExecutor = Executors.newFixedThreadPool(DEFAULT_THREADS);
    private static ScheduledExecutorService mScheduledExecutorService = Executors.newScheduledThreadPool(DEFAULT_SCHEDULED_THREADS);

    public static <R> void notifyStartListener(final ExecutorListener<R> listener) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.start();
            }
        });
    }

    public static <R> void notifyErrorListener(final ExecutorListener<R> listener, final Exception e) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.error(e);
            }
        });
    }

    public static <R> void notifyCompleteListener(final ExecutorListener<R> listener, final R data) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.complete(data);
            }
        });
    }

    public static ScheduledFuture<?> doTaskWithInterval(Runnable command,
                                                        long initialDelay,
                                                        long period,
                                                        TimeUnit unit) {
        if (command != null) {
            return mScheduledExecutorService.scheduleAtFixedRate(command, initialDelay, period, unit);
        }
        return null;
    }

    public static <V> void doNetworkTaskAsync(final Callable<V> task, final ExecutorListener<V> listener) {

        Runnable runnable = wrapToListener(task, listener);
        if (runnable != null){
            mNetworkExecutor.submit(runnable);
        }
    }

    public static <V> void doBackgroundTaskAsync(final Callable<V> task, final ExecutorListener<V> listener) {

        Runnable runnable = wrapToListener(task, listener);
        if (runnable != null){
            mThreadPoolExecutor.submit(runnable);
        }
    }

    private static <V> Runnable wrapToListener(final Callable<V> task, final ExecutorListener<V> listener) {
        if (task != null && listener != null) {
            return new Runnable() {
                @Override
                public void run() {
                    try {
                        notifyStartListener(listener);
                        V result = task.call();
                        notifyCompleteListener(listener, result);
                    } catch (Exception e) {
                        notifyErrorListener(listener, e);
                    }
                }
            };
        }
        return null;
    }
}
