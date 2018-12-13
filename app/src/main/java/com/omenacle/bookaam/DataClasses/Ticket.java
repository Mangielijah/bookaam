package com.omenacle.bookaam.DataClasses;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "ticket")
public class Ticket {


    //Agency (agency Key)
    @NonNull
    @ColumnInfo(name = "a")
    private String a;
    //Branch (branch key)
    @NonNull
    @ColumnInfo(name = "b")
    private String b;
    //Ticket Code
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "code")
    private long code;
    //Travel date
    @ColumnInfo(name = "date")
    @NonNull
    private String date;
    //Name of ticket bearer
    @ColumnInfo(name = "name")
    @NonNull
    private String name;
    //Number of ticket bearer
    @ColumnInfo(name = "num")
    @NonNull
    private long num;
    //Id card number of bearer
    @ColumnInfo(name = "nid")
    private long id;
    //Travel route (e.g limbe->bamenda)
    @ColumnInfo(name = "r")
    @NonNull
    private String r;

    //Travel time (e.g Morning, Afternoon, Night)
    @ColumnInfo(name = "time")
    private String time;

    public Ticket(@NonNull String a, @NonNull String b, @NonNull long code, @NonNull String date, @NonNull String name, @NonNull long num, long id, @NonNull String r, String time) {
        this.a = a;
        this.b = b;
        this.code = code;
        this.date = date;
        this.name = name;
        this.num = num;
        this.id = id;
        this.r = r;
        this.time = time;
    }

    //Public getters
    public String getA() {
        return a;
    }

    public String getB() {
        return b;
    }

    public long getCode() {
        return code;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public long getNum() {
        return num;
    }

    public long getId() {
        return id;
    }

    public String getR() {
        return r;
    }

    public String getTime() {
        return time;
    }


    //Public Setters
    public void setA(String a) {
        this.a = a;
    }

    public void setB(String b) { this.b = b; }

    public void setCode(@NonNull long code) {
        this.code = code;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setR(String r) {
        this.r = r;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
