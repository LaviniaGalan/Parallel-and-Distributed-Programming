package com.company;

import com.company.Split.ByColumn;
import com.company.Split.ByKthElement;
import com.company.Split.ByRow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    static int n1 = 5, m1 = 5;
    static int n2 = 5, m2 = 5;

    static Matrix matrix1 = new Matrix(n1, m1, false);
    static Matrix matrix2 = new Matrix(n2, m2, false);

    static Matrix resultMatrix = new Matrix(n1, m2, true);

    static int NO_TASKS = 4;

    public static void main(String[] args) throws InterruptedException {

//        System.out.println(matrix1);
//        System.out.println(matrix2);

        List<Thread> rowThreads = splitTasksByRow();
        normalThreads(rowThreads, "Row");
        threadPool(rowThreads, "Row");

        List<Thread> columnThreads = splitTasksByColumn();
        normalThreads(columnThreads, "Column");
        threadPool(columnThreads, "Column");


        List<Thread> kThreads = splitTasksByK();
        normalThreads(kThreads, "Kth");
        threadPool(kThreads, "Kth");


    }

    public static void normalThreads(List<Thread> threads, String split) throws InterruptedException {
        System.out.println("> " + split + " Split, Normal Threads:");
        long start = System.currentTimeMillis();

        threads.forEach(Thread::start);
        for(Thread t: threads) {
            t.join();
        }
        long end = System.currentTimeMillis();

        System.out.println("Time = " + (end - start) + " ms.");
        System.out.println(resultMatrix);
    }

    public static void threadPool(List<Thread> threads, String split) throws InterruptedException {
        System.out.println("> " + split + " Split, Thread Pool: ");
        long start = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(NO_TASKS);
        threads.forEach(executorService::submit);
        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(500, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            ex.printStackTrace();
            Thread.currentThread().interrupt();
        }

        long end = System.currentTimeMillis();
        System.out.println("Time = " + (end - start) + " ms.");
        System.out.println(resultMatrix);
    }


    public static List<Thread> splitTasksByRow(){
        List<Thread> rowThreads = new ArrayList<>();

        int noElementsInResult = n1 * m2;

        int noElementsPerTask = noElementsInResult / NO_TASKS;

        int i, j;

        for(int index = 0; index < NO_TASKS; index++){
            i = noElementsPerTask * index / m2;
            j = noElementsPerTask * index % m2;

            if(index == NO_TASKS - 1) {
                noElementsPerTask = noElementsInResult - (NO_TASKS - 1) * noElementsPerTask;
            }
            ByRow thread = new ByRow(i, j, noElementsPerTask, resultMatrix,
                    matrix1, matrix2);
            rowThreads.add(thread);
        }
        return rowThreads;
    }


    public static List<Thread> splitTasksByColumn(){
        List<Thread> columnThreads = new ArrayList<>();

        int noElementsInResult = n1 * m2;

        int noElementsPerTask = noElementsInResult / NO_TASKS;

        int i, j;

        for(int index = 0; index < NO_TASKS; index++){
            i = noElementsPerTask * index % n1;
            j = noElementsPerTask * index / n1;

            if(index == NO_TASKS - 1) {
                noElementsPerTask = noElementsInResult - (NO_TASKS - 1) * noElementsPerTask;
            }
            ByColumn thread = new ByColumn(i, j, noElementsPerTask, resultMatrix,
                    matrix1, matrix2);
            columnThreads.add(thread);
        }
        return columnThreads;
    }

    public static List<Thread> splitTasksByK(){
        List<Thread> kThreads = new ArrayList<>();
        for(int index = 0; index < NO_TASKS; index++){
            ByKthElement thread = new ByKthElement(index, NO_TASKS, resultMatrix,
                    matrix1, matrix2);
            kThreads.add(thread);
        }
        return kThreads;
    }

}
