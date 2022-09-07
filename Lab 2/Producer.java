package com.company;

import java.util.Arrays;
import java.util.List;

public class Producer extends Thread{

    private List<Integer> v1 = Arrays.asList(10, 12, 12, 1, 4, 5);
    private List<Integer> v2 = Arrays.asList(7, 12, 1, 100, 6, 18);

    private Buffer buffer;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run(){
        for(int i = 0; i < v1.size(); i++){
            int product = v1.get(i) * v2.get(i);
            try {
                System.out.println("> Producer: sends to the buffer the value " + product + ", a1 = " + v1.get(i) + ", a2 = " + v2.get(i));
                buffer.putValueInBuffer(product);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getLength(){
        return v1.size();
    }

}
