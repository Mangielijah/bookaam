package com.omenacle.bookaam.DataClasses;

public class Seats {
    public Seats(String a_k, long m, long a, long n, String r_k) {
        this.a_k = a_k;
        this.m = m;
        this.a = a;
        this.n = n;
        this.r_k = r_k;
    }

    private String a_k;

    private long m = 0;

    private long a = 0;

    private long n = 0;

    public String getR_k() {
        return r_k;
    }

    public void setR_k(String r_k) {
        this.r_k = r_k;
    }

    private String r_k;

    public Seats() {

    }

    public String getA_k() {
        return a_k;
    }

    public void setA_k(String a_k) {
        this.a_k = a_k;
    }

    public long getM() {
        return m;
    }

    public void setM(long m) {
        this.m = m;
    }

    public long getA() {
        return a;
    }

    public void setA(long a) {
        this.a = a;
    }

    public long getN() {
        return n;
    }

    public void setN(long n) {
        this.n = n;
    }
}
