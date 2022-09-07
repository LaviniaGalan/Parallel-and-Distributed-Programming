package com.company.Split;

import com.company.Matrix;


public class ByKthElement extends Thread {

    int k;
    int start;
    Matrix resultMatrix;
    Matrix matrix1;
    Matrix matrix2;

    public ByKthElement(int start, int k, Matrix resultMatrix, Matrix matrix1, Matrix matrix2) {

        this.start = start;
        this.k = k;
        this.resultMatrix = resultMatrix;
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
    }

    public int computeResult(int row, int column) {
        int result = 0;
        for(int i = 0; i < matrix1.m; i++){
            result += matrix1.get(row, i) * matrix2.get(i, column);
        }
        return result;
    }

    @Override
    public void run(){
        int i = start / resultMatrix.m;
        int j = start % resultMatrix.m;

        while(i < this.resultMatrix.n){
            int result = computeResult(i, j);
            this.resultMatrix.set(i, j, result);

            i += (j + k) / resultMatrix.m;
            j = (j + k) % resultMatrix.m;
        }
    }

}
