package com.company;

public class Consumer extends Thread {

    private int sum = 0;
    private int vectorSize;

    private Buffer buffer;

    public Consumer(Buffer buffer, int vectorSize) {
        this.buffer = buffer;
        this.vectorSize = vectorSize;
    }

    @Override
    public void run(){
        for(int i = 0; i < vectorSize; i++){
            try {
                int value = this.buffer.getValueFromBuffer();
                sum += value;
                System.out.println("> Consumer gets from the buffer the value " + value + ", sum = " + sum);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
