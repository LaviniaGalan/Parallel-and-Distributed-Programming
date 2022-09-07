package com.company;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {

    private Lock mutex = new ReentrantLock();
    private Condition conditionalVariable = mutex.newCondition();

    private Queue<Integer> queue = new LinkedList<>();

    private int maxCapacity = 1;

    public void putValueInBuffer(Integer value) throws InterruptedException {
        mutex.lock();
        while (queue.size() == this.maxCapacity){
            conditionalVariable.await();
        }
        queue.add(value);
        System.out.println("> Buffer: The value " + value + " added into the buffer.");
        conditionalVariable.signal();

        mutex.unlock();
    }

    public Integer getValueFromBuffer() throws InterruptedException {
        mutex.lock();
        while (queue.size() == 0){
            conditionalVariable.await();
        }
        int value = queue.poll();
        System.out.println("> Buffer: The value " + value + " extracted from the buffer.");

        conditionalVariable.signal();

        mutex.unlock();
        return value;
    }
}
