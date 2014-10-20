package com.droidutils.multithreading;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * Created by Mkhitar on 14.10.2014.
 */
public class CustomSemaphore {

    private Map<String, Semaphore> mRunningTask;

    public CustomSemaphore(){
        mRunningTask = new HashMap<String, Semaphore>();
    }

    public void acquire(String taskTag) throws InterruptedException {

        Semaphore semaphore = null;
        if (!mRunningTask.containsKey(taskTag)) {
            semaphore = new Semaphore(1);
        } else {
            semaphore = mRunningTask.get(taskTag);
        }
        semaphore.acquire();
        mRunningTask.put(taskTag, semaphore);
    }

    public void release(String taskTag) throws InterruptedException {

        if (mRunningTask.containsKey(taskTag)) {
            mRunningTask.remove(taskTag).release();
        }
    }

    public void clear(){
        mRunningTask.clear();
    }
}
