package com.company;

public class Main {

    public static void main(String[] args) {

        Buffer buffer = new Buffer();
        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer, producer.getLength());

        producer.start();
        consumer.start();
    }
}
