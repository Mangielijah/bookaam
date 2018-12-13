package com.omenacle.bookaam.DataClasses;

public class Agency {


    //agency key
    private String a_k;
    //agency name
    private String a_n;

    public Agency(){

        // Default constructor required for calls to DataSnapshot.getValue(Agency.class)

    }

    public Agency(String a_k, String a_n) {
        this.a_k = a_k;
        this.a_n = a_n;
    }
    //Get Agency Key
    public String getA_k() {
        return a_k;
    }

    //Set Agency key
    public void setA_k(String a_k) {
        this.a_k = a_k;
    }

    //Get Agency Name
    public String getA_n() {
        return a_n;
    }

    //Set Agency Name
    public void setA_n(String a_n) {
        this.a_n = a_n;
    }

    @Override
    public String toString() {
        return "Key" + getA_k() + " Name" + getA_n();
    }
}
