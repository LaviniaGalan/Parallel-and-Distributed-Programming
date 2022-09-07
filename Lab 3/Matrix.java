package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Matrix {
    List<List<Integer>> matrix = new ArrayList<>();

    public int n;
    public int m;

    private static final int BOUND  = 10;
    private final Random random = new Random();


    public Matrix(int n, int m, boolean isResult) {
        this.n = n;
        this.m = m;
        this.generateMatrix(isResult);
    }

    public void generateMatrix(boolean isResult){
        if(isResult){
            for(int i = 0; i < n; i++){
                matrix.add(new ArrayList<>());
                for(int j = 0; j < m; j++){
                    matrix.get(i).add(0);
                }
            }
        }
        else{
            for(int i = 0; i < n; i++){
                matrix.add(new ArrayList<>());
                for(int j = 0; j < m; j++){
                    matrix.get(i).add(random.nextInt(BOUND) + 1);
                }
            }
        }
    }

    public Integer get(int i, int j){
        return this.matrix.get(i).get(j);
    }

    public void set(int i, int j, int value){
        this.matrix.get(i).set(j, value);
    }

    @Override
    public String toString() {
        String result = "";
        for(int i = 0; i < n;i++) {
            for (int j = 0; j < m; j++)
                result += matrix.get(i).get(j) + " ";
            result += "\n";
        }
        return result;
    }
}
