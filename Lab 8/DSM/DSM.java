package com.company.DSM;

import com.company.Messages.ChangeVariableMessage;
import com.company.Messages.CloseMessage;
import com.company.Messages.Message;
import com.company.Messages.SubscribeMessage;
import mpi.MPI;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DSM {
    private Map<String, Integer> variables = new ConcurrentHashMap<>();
    private Map<String, Set<Integer>> subscriptions = new ConcurrentHashMap<>();
    private static Lock lock = new ReentrantLock();

    public DSM(){
        variables.put("a", 0);
        variables.put("b", 0);
        variables.put("c", 0);

        for(String v: variables.keySet()){
            subscriptions.put(v, new HashSet<>());
        }
    }

    public void addSubscription(String variable, Integer subscriber){
        subscriptions.get(variable).add(subscriber);
    }

    public void removeSubscription(Integer subscriber){

        for(String variable: subscriptions.keySet()){
            subscriptions.get(variable).remove(subscriber);
        }

    }

    public void updateVariable(String variable, Integer value){
        variables.put(variable, value);
    }

    public void subscribe(String variable){

        int rank = MPI.COMM_WORLD.Rank();
        //System.out.println("> " + rank + " subscribing to " + variable + "...");
        addSubscription(variable, rank);
        Message message = new SubscribeMessage(variable, MPI.COMM_WORLD.Rank());
        notifyAll(message);
    }

    public void writeValueToVariable(String variable, Integer value){
        lock.lock();
        updateVariable(variable, value);
        Message message = new ChangeVariableMessage(variable, value, MPI.COMM_WORLD.Rank());
        notifyOtherSubscribers(variable, message);
        System.out.println("> " + MPI.COMM_WORLD.Rank() + " changing " + variable + " to " + value + "...");
        lock.unlock();
    }

    public void compareAndExchange(String variable, Integer oldValue, Integer newValue){

        if(variables.get(variable).equals(oldValue)){
            writeValueToVariable(variable, newValue);
        }
    }

    public void close() {
        int rank = MPI.COMM_WORLD.Rank();
        System.out.println("> " + rank + " closing dsm...");
        Message message = new CloseMessage(MPI.COMM_WORLD.Rank());

        notifyAll(message);
    }

    public void notifyOtherSubscribers(String variable, Message message){
        List<Integer> allProcesses = IntStream.range(0, MPI.COMM_WORLD.Size()).boxed().collect(Collectors.toList());

        for(Integer process: allProcesses){
            if(process != MPI.COMM_WORLD.Rank() && subscriptions.get(variable).contains(process)){
                Message[] buffer = new Message[1];
                buffer[0] = message;
                MPI.COMM_WORLD.Send(buffer, 0, 1, MPI.OBJECT, process, 0);
            }
        }
    }

    public void notifyAll(Message message){
        List<Integer> allProcesses = IntStream.range(0, MPI.COMM_WORLD.Size()).boxed().collect(Collectors.toList());

        for(Integer process: allProcesses){
            Message[] buffer = new Message[1];
            buffer[0] = message;
            MPI.COMM_WORLD.Send(buffer, 0, 1, MPI.OBJECT, process, 0);
        }
    }

    @Override
    public String toString() {
        return MPI.COMM_WORLD.Rank() + " DSM{" +
                "variables=" + variables +
                ", subscriptions=" + subscriptions +
                '}';
    }
}
