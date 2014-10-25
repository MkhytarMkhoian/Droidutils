package com.droidutils.multithreading;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * Created by Mkhitar on 14.10.2014.
 */
public class CustomSemaphore {

    private Map<String, Semaphore> mRunningTask;

    public CustomSemaphore(){
        mRunningTask = new ConcurrentHashMap<String, Semaphore>();
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

 //       Log.e("CustomSemaphore acquire size", mRunningTask.size() + "");
    }

    public void release(String taskTag) throws InterruptedException {

        if (mRunningTask.containsKey(taskTag)) {
            mRunningTask.remove(taskTag).release();
        }
 //       Log.e("CustomSemaphore release size", mRunningTask.size() + "");
    }

    public void clear(){
        mRunningTask.clear();
    }
}
