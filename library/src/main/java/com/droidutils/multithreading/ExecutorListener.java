package com.droidutils.multithreading;

/**
 * Created by Misha on 06.09.2014.
 */
public abstract class ExecutorListener<R> {

    public abstract void start();
    public abstract void complete(R result);
    public abstract void error(Exception e);
}
