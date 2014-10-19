package com.droidutils.multithreading;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * Created by Mkhitar on 14.10.2014.
 */
public class CustomSemaphore {

    private Map<Integer, Semaphore> mRunningTask;

    public CustomSemaphore(){
        mRunningTask = new HashMap<Integer, Semaphore>();
    }

    public void acquire(int taskTag) throws InterruptedException {

        if (!mRunningTask.containsKey(taskTag)) {
            final Semaphore semaphore = new Semaphore(1);
            mRunningTask.put(taskTag, semaphore);
            semaphore.acquire();
        } else {
            mRunningTask.get(taskTag).acquire();
        }
    }

    public void release(int taskTag) throws InterruptedException {

        if (mRunningTask.containsKey(taskTag)) {
            mRunningTask.get(taskTag).release();
        }
    }

    public void clear(){
        mRunningTask.clear();
    }
}
