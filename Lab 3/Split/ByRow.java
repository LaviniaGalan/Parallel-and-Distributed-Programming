package com.company.Split;

import com.company.Matrix;


public class ByRow extends Thread {

    int startRow, startColumn;
    int nrElements;
    Matrix resultMatrix;
    Matrix matrix1;
    Matrix matrix2;

    public ByRow(int startRow, int startColumn, int nrElements, Matrix resultMatrix, Matrix matrix1, Matrix matrix2) {

        this.startRow = startRow;
        this.startColumn = startColumn;
        this.nrElements = nrElements;

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
        int i = this.startRow;
        int j = this.startColumn;
        int nrElements = this.nrElements;

        while(nrElements > 0 && i < this.resultMatrix.n && j < this.resultMatrix.m){
            int result = computeResult(i, j);
            this.resultMatrix.set(i, j, result);

            j++;
            if(j == this.resultMatrix.m) {
                j = 0;
                i++;
            }
            nrElements--;
        }
    }

}
