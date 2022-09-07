package com.company;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Implementation {

    ExecutorService executorService;
    Lock mutex = new ReentrantLock();

    List<Integer> result;

    AtomicBoolean cycleFound = new AtomicBoolean(false);

    public Implementation(){

    }

    public void run(Graph graph) throws InterruptedException {

        executorService = Executors.newFixedThreadPool(graph.getNodes().size());

        for(int i = 0; i < graph.getNodes().size(); i++){
            int finalI = i;
            executorService.submit(()->{
                detectCycle(graph.getNodes().get(finalI), graph.getNodes().get(finalI), new ArrayList<>(), graph);
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(60, TimeUnit.SECONDS);

        System.out.println("Cycle found: " + cycleFound.get());
        System.out.println(result);

        if(cycleFound.get() && ! graph.checkValidity(result)){
            System.out.println("oops");
        }
    }

    public void detectCycle(int startingNode, int currentNode, List<Integer> path, Graph graph){

        path.add(currentNode);

        if(!cycleFound.get()){
            if(path.size() == graph.getNodes().size()){
                if(graph.getNextNodes(currentNode).contains(startingNode)){
                    path.add(startingNode);
                    mutex.lock();
                    result = path;
                    mutex.unlock();
                    cycleFound.set(true);
                }

            }
            else{
                List<Integer> nextNodes = graph.getNextNodes(currentNode);
                for (Integer nextNode : nextNodes) {
                    if(! path.contains(nextNode)){
                        detectCycle(startingNode, nextNode, new ArrayList<>(path), graph);
                    }
                }
            }
        }

    }

}


