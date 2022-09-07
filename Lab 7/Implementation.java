package com.company;

public class Implementation {

    public static Polynomial simpleMultiplication(Polynomial p, Polynomial q, int start, int end){

        Polynomial r = new Polynomial(p.getN(), q.getN());

        for (int i = start; i < end; i++) {
            for (int j = 0; j < q.getN(); j++) {
                r.setCoefficient(i + j, r.getCoefficient(i + j) + p.getCoefficient(i) * q.getCoefficient(j));
            }
        }
        return r;
    }


    public static Polynomial karatsubaMultiplication(Polynomial p, Polynomial q, int depth){

        if (p.getN() <= 2 || q.getN() <= 2 || depth >= 4){
            return simpleMultiplication(p, q,0, p.getN());
        }
        else{
            int maxCoefficients = Math.max(p.getN(), q.getN());
            int length = maxCoefficients / 2;

            Polynomial p1 = new Polynomial(p.coefficients.subList(0, length));
            Polynomial p2 = new Polynomial(p.coefficients.subList(length, p.getN()));

            Polynomial q1 = new Polynomial(q.coefficients.subList(0, length));
            Polynomial q2 = new Polynomial(q.coefficients.subList(length, q.getN()));

            Polynomial r1 = karatsubaMultiplication(p1, q1, depth + 1);
            Polynomial r2 = karatsubaMultiplication(add(p1, p2), add(q1, q2), depth + 1);
            Polynomial r3 = karatsubaMultiplication(p2, q2, depth + 1);

            return mergeResults(r1, r2, r3, length);
        }
    }


    public static Polynomial add(Polynomial p, Polynomial q){
        return p.add(q);
    }

    public static Polynomial mergeResults(Polynomial r1, Polynomial r2, Polynomial r3, int len){

        Polynomial aux1 = r3.addNullCoefficients(2 * len);

        Polynomial difference = r2.subtract(r3).subtract(r1);
        Polynomial aux2 = difference.addNullCoefficients(len);

        Polynomial result = aux1.add(aux2).add(r1);
        return result;
    }


}
