package com.omenacle.bookaam.DataClasses;

public class BookaamInfo {

    //Platform charge
    private long c;
    //email
    private String e;
    //password
    private  String p;
    //Key
    private String k;
    //number
    private long n;

    public long getVc() {
        return vc;
    }

    public void setVc(long vc) {
        this.vc = vc;
    }

    //Vip ticket charge
    private long vc;


    public BookaamInfo(){

    }

    public BookaamInfo(long c, String e, String p, String k, long n, long vc) {
        this.c = c;
        this.e = e;
        this.p = p;
        this.k = k;
        this.n = n;
        this.vc = vc;
    }


    public long getC() {
        return c;
    }

    public void setC(long c) {
        this.c = c;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public long getN() {
        return n;
    }

    public void setN(long n) {
        this.n = n;
    }
}
