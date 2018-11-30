package com.omenacle.bookaam.DataClasses;

public class BranchInfo {

    //email
    private String e;
    //password
    private  String p;
    //Key
    private String k;
    //number
    private long n;


    public BranchInfo(){

    }

    public BranchInfo(String e, String p, String k, long n) {
        this.e = e;
        this.p = p;
        this.k = k;
        this.n = n;
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
