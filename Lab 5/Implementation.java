package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Implementation {

    public void simpleMultiplication(Polynomial p, Polynomial q, Polynomial r, int begin, int end){

        for(int k = begin; k <= end; k++){
            int s = 0;
            for(int i = 0; i <= k; i++){
                if(i < p.getN() && k - i < q.getN()){
                    s += p.getCoefficient(i) * q.getCoefficient(k-i);
                }
            }

            r.setCoefficient(k, s);
        }
    }

    public void simpleSequential(Polynomial p, Polynomial q, Polynomial r){
        simpleMultiplication(p, q, r, 0, p.getN() + q.getN() - 2);
    }

    public void simpleParallelized(Polynomial p, Polynomial q, Polynomial r, int nrThreads){
        List<Callable<Object>> tasks = new ArrayList<>();

        int elementsPerThread = (p.getN() + q.getN() - 2) / nrThreads;

        for(int i = 0; i < nrThreads; i++){
            int begin = i * elementsPerThread;
            int end = begin + elementsPerThread - 1;
            if(i == nrThreads - 1){
                end = p.getN() + q.getN() - 2;
            }
            int finalEnd = end;
            Callable<Object> task = Executors.callable(() -> {
                simpleMultiplication(p, q, r, begin, finalEnd);
            });
            tasks.add(task);
            // System.out.println(begin + " - " + finalEnd);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(nrThreads);
        tasks.forEach(executorService::submit);
        executorService.shutdown();

        try{
            executorService.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }


    public Polynomial karatsuba(Polynomial p, Polynomial q){
        if (p.getN() <= 2 || q.getN() <= 2){
            Polynomial r = new Polynomial(p.getN(), q.getN());
            simpleMultiplication(p, q, r, 0, r.getN() - 1);
            return r;
        }
        else{
            int maxCoefficients = Math.max(p.getN(), q.getN());
            int length = maxCoefficients / 2;

            Polynomial p1 = new Polynomial(p.coefficients.subList(0, length));
            Polynomial p2 = new Polynomial(p.coefficients.subList(length, p.getN()));

            Polynomial q1 = new Polynomial(q.coefficients.subList(0, length));
            Polynomial q2 = new Polynomial(q.coefficients.subList(length, q.getN()));

            Polynomial r1 = karatsuba(p1, q1);
            Polynomial r2 = karatsuba(add(p1, p2), add(q1, q2));
            Polynomial r3 = karatsuba(p2, q2);

            return mergeResults(r1, r2, r3, length);

        }
    }

    public Polynomial mergeResults(Polynomial r1, Polynomial r2, Polynomial r3, int len){

        Polynomial aux1 = r3.addNullCoefficients(2 * len);

        Polynomial difference = r2.subtract(r3).subtract(r1);
        Polynomial aux2 = difference.addNullCoefficients(len);

        Polynomial result = aux1.add(aux2).add(r1);
        return result;
    }

    public Polynomial KaratsubaSequential(Polynomial p, Polynomial q){
        return this.karatsuba(p, q);
    }

    public Polynomial add(Polynomial p, Polynomial q){
        return p.add(q);
    }

    public Polynomial KaratsubaParallelized(Polynomial p, Polynomial q){
        if (p.getN() <= 2 || q.getN() <= 2){
            Polynomial r = new Polynomial(p.getN(), q.getN());
            simpleMultiplication(p, q, r, 0, r.getN() - 1);
            return r;
        }
        else{
            int maxCoefficients = Math.max(p.getN(), q.getN());
            int length = maxCoefficients / 2;

            Polynomial p1 = new Polynomial(p.coefficients.subList(0, length));
            Polynomial p2 = new Polynomial(p.coefficients.subList(length, p.getN()));

            Polynomial q1 = new Polynomial(q.coefficients.subList(0, length));
            Polynomial q2 = new Polynomial(q.coefficients.subList(length, q.getN()));

            ExecutorService executorService = Executors.newCachedThreadPool();

            Future<Polynomial> f1 = executorService.submit(() -> karatsuba(p1, q1));
            Future<Polynomial> f2 = executorService.submit(() -> karatsuba(add(p1, p2), add(q1, q2)));
            Future<Polynomial> f3 = executorService.submit(() -> karatsuba(p2, q2));
            executorService.shutdown();

            try {
                Polynomial r1 = f1.get();
                Polynomial r2 = f2.get();
                Polynomial r3 = f3.get();
                executorService.awaitTermination(60, TimeUnit.SECONDS);

                return mergeResults(r1, r2, r3, length);

            } catch (InterruptedException | ExecutionException e) {
                System.out.println(e.getMessage());
            }
        }

        return new Polynomial();
    }
}
