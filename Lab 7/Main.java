package com.company;

import mpi.*;
public class Main {

    public static int n = 1000;
    public static int mode = 0;

    public static void main(String[] args) {

        MPI.Init(args);

        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        //System.out.println(me + " la inceput");

        if(me == 0){
            mainProcess(size);
        }
        else {
            if(mode == 0){
                simpleProcess();
            }
            else{
                karatsubaProcess();
            }
        }

        //System.out.println(me + " la sfarsit");
        MPI.Finalize();
    }

    public static void mainProcess(int size){
        Polynomial p = new Polynomial(n);
        Polynomial q = new Polynomial(n);


        long startTime = System.currentTimeMillis();
        int elementsPerProcess = p.getN() / size;

        int start, end = 0;
        for(int i = 0; i < size - 1; i++) {
            start = end;
            end = end + elementsPerProcess;
            if (i == size - 2) {
                end = p.getN();
            }

            Wrapper wrapper = new Wrapper(p, q, start, end);
            Object[] w = new Object[] { wrapper };
            MPI.COMM_WORLD.Send(w,0, 1, MPI.OBJECT, i + 1, 0);
        }

        Polynomial finalResult = new Polynomial(p.getN(), q.getN());
        for(int i = 0; i < size - 1; i++){
            Object[] buffer = new Object[1];
            MPI.COMM_WORLD.Recv(buffer, 0, 1, MPI.OBJECT, MPI.ANY_SOURCE, 0);
            Polynomial r = (Polynomial) buffer[0];
            finalResult = finalResult.add(r);
        }

        long endTime = System.currentTimeMillis();

        System.out.println("Total time: " + (endTime - startTime) + " ms.");

//        System.out.println(p);
//        System.out.println(q);
        //System.out.println("0 > Final result = " + finalResult);

        var test = Implementation.simpleMultiplication(p, q, 0, p.getN());
        System.out.println("0 > Final result = " + test);
        System.out.println(finalResult.equals(test));

    }

    public static void simpleProcess(){
        Object[] buffer = new Object[1];
        MPI.COMM_WORLD.Recv(buffer, 0, 1, MPI.OBJECT, 0, 0);

        if(buffer[0] != null){
            Wrapper wrapper = (Wrapper) buffer[0];
            Polynomial p = wrapper.p;
            Polynomial q = wrapper.q;
            int start = wrapper.start;
            int end = wrapper.end;

            Polynomial r = Implementation.simpleMultiplication(p, q, start, end);
            //System.out.println(r);

            Object[] w = new Object[] { r };
            MPI.COMM_WORLD.Send(w,0, 1, MPI.OBJECT, 0, 0);
        }
    }

    public static void karatsubaProcess(){
        Object[] buffer = new Object[1];
        MPI.COMM_WORLD.Recv(buffer, 0, 1, MPI.OBJECT, 0, 0);

        if(buffer[0] != null){
            Wrapper wrapper = (Wrapper) buffer[0];
            Polynomial p = wrapper.p;
            Polynomial q = wrapper.q;

            int start = wrapper.start;
            int end = wrapper.end;

            for(int i = 0; i < start; i++){
                p.setCoefficient(i, 0);
            }
            for(int i = end; i < p.getN(); i++){
                p.setCoefficient(i, 0);
            }

            Polynomial r = Implementation.karatsubaMultiplication(p, q, 0);
            //System.out.println(r);

            Object[] w = new Object[] { r };
            MPI.COMM_WORLD.Send(w,0, 1, MPI.OBJECT, 0, 0);
        }
    }
}
