package com.company;

import java.util.concurrent.ExecutionException;

public class Main {

    static int n = 20;
    static int m = 50;

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Graph graph = new Graph(n, m);

        Implementation impl = new Implementation();

        impl.run(graph);


    }
}
