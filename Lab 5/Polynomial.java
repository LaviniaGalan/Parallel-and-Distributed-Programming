package com.company;

import java.util.*;

public class Polynomial {

    List<Integer> coefficients;

    int n;

    static int BOUND = 10;

    public Polynomial(int n){
        this.n = n;
        this.coefficients = new ArrayList<>();
        this.initializeCoefficients();
    }

    public Polynomial(int n, int m){
        this.n = n + m - 1;
        this.coefficients = new ArrayList<>(Collections.nCopies(this.n, 0));
    }

    public Polynomial(List<Integer> coefficients){
        this.n = coefficients.size();
        this.coefficients = coefficients;
    }

    public Polynomial(int n, boolean needsInitialization){
        this.n = n;
        if(needsInitialization){
            this.coefficients = new ArrayList<>();
            this.initializeCoefficients();
        }
        else{
            this.coefficients = new ArrayList<>(Collections.nCopies(this.n, 0));
        }
    }

    public Polynomial() {

    }

    public void initializeCoefficients(){
        Random random = new Random();
        for(int i = 0; i < n; i++){
            coefficients.add(random.nextInt(BOUND) + 1);
        }
    }

    public void setCoefficient(int i, int value){
        this.coefficients.set(i, value);
    }

    public int getN(){
        return this.n;
    }

    public int getCoefficient(int i){
        return this.coefficients.get(i);
    }

    public Polynomial add(Polynomial other){
        int rN = Math.max(this.n, other.n);
        Polynomial r = new Polynomial(rN, false);

        for(int i = 0; i < rN; i++){
            int value = 0;
            if(i < this.n){
                value += this.coefficients.get(i);
            }
            if(i < other.n){
                value += other.coefficients.get(i);
            }
            r.setCoefficient(i, value);
        }
        return r;
    }

    public Polynomial subtract(Polynomial other){
        int rN = Math.max(this.n, other.n);

        Polynomial r = new Polynomial(rN, false);

        for(int i = 0; i < rN; i++){
            int value = 0;
            if(i < this.coefficients.size()){
                value += this.coefficients.get(i);
            }
            if(i < other.coefficients.size()){
                value -= other.coefficients.get(i);
            }
            r.setCoefficient(i, value);
        }

        int i = r.coefficients.size() - 1;
        while (i > 0 && r.coefficients.get(i) == 0) {
            r.coefficients.remove(i);
            i--;
        }
        this.n = coefficients.size();

        return r;
    }

    public Polynomial concatenate(Polynomial p){
        Polynomial r = new Polynomial(this.coefficients);
        r.coefficients.addAll(p.coefficients);
        return r;
    }

    public Polynomial addNullCoefficients(int nr){
        List<Integer> newCoefficients =  new ArrayList<>(Collections.nCopies(nr, 0));
        newCoefficients.addAll(this.coefficients);
        return new Polynomial(newCoefficients);
    }

    @Override
    public String toString() {
        return "Polynomial{" +
                "coefficients=" + coefficients +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Polynomial that = (Polynomial) o;
        return n == that.n && Objects.equals(coefficients, that.coefficients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coefficients, n);
    }
}

