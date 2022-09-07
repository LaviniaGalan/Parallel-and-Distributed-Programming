package com.company;

public class Main {

    static int NR_THREADS = 4;

    static int n = 1000;
    static int m = 1000;

    public static void main(String[] args) throws InterruptedException {


        Polynomial p = new Polynomial(n);
        Polynomial q = new Polynomial(m);

        Polynomial ss = simpleSequential(p, q);
        Polynomial sp = simpleParallelized(p, q);
        Polynomial ks = KaratsubaSequential(p, q);
        Polynomial kp = KaratsubaParallelized(p, q);

        checkEquality(ss, sp, ks, kp);

//        System.out.println(ss);
//        System.out.println(sp);
//        System.out.println(ks);
//        System.out.println(kp);
//        System.out.println(p);
//        System.out.println(q);
    }

    public static Polynomial simpleSequential(Polynomial p, Polynomial q) {
        Implementation implementation = new Implementation();
        Polynomial r = new Polynomial(n, m);

        long start = System.currentTimeMillis();
        implementation.simpleSequential(p, q, r);
        long end = System.currentTimeMillis();
        System.out.println("Simple sequential: Time = " + (end - start) + " ms.");

        //System.out.println(r);
        return r;
    }

    public static Polynomial simpleParallelized(Polynomial p, Polynomial q) throws InterruptedException {
        Implementation implementation = new Implementation();
        Polynomial r = new Polynomial(n, m);

        long start = System.currentTimeMillis();
        implementation.simpleParallelized(p, q, r, NR_THREADS);
        long end = System.currentTimeMillis();
        System.out.println("Simple parallelized: Time = " + (end - start) + " ms.");

        return r;

    }


    public static Polynomial KaratsubaSequential(Polynomial p, Polynomial q){
        Implementation implementation = new Implementation();
        Polynomial r;

        long start = System.currentTimeMillis();
        r = implementation.KaratsubaSequential(p, q);
        long end = System.currentTimeMillis();
        System.out.println("Karatsuba sequential: Time = " + (end - start) + " ms.");

        //System.out.println(r);
        return r;
    }

    public static Polynomial KaratsubaParallelized(Polynomial p, Polynomial q) {
        Implementation implementation = new Implementation();
        Polynomial r;

        long start = System.currentTimeMillis();
        r = implementation.KaratsubaParallelized(p, q);
        long end = System.currentTimeMillis();
        System.out.println("Karatsuba parallelized: Time = " + (end - start) + " ms.");

        //System.out.println(r);
        return r;
    }

    public static void checkEquality(Polynomial ss, Polynomial sp, Polynomial ks, Polynomial kp){
        System.out.println("Simple sequential = Simple parallelized: " + ss.equals(sp));
        System.out.println("Simple parallelized = Karatsuba sequential: " + sp.equals(ks));
        System.out.println("Karatsuba sequential = Karatsuba parallelized: " + ks.equals(kp));
    }

}
