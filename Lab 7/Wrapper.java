package com.company;

import java.io.Serializable;

public class Wrapper implements Serializable {

    Polynomial p;
    Polynomial q;
    Polynomial r;
    int start;
    int end;

    public Wrapper(Polynomial p, Polynomial q, int start, int end) {
        this.p = p;
        this.q = q;
        this.start = start;
        this.end = end;
    }

    public Wrapper(Polynomial p, Polynomial q, Polynomial r, int start, int end) {
        this.p = p;
        this.q = q;
        this.r = r;
        this.start = start;
        this.end = end;
    }
}
