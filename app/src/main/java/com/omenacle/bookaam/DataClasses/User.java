package com.omenacle.bookaam.DataClasses;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "user")
public class User {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    //user name
    @NonNull
    @ColumnInfo(name = "name")
    private String name;
    //phone number of user
    @NonNull
    @ColumnInfo(name = "number")
    private String number;
    //Email address
    private String email;

    public User(){

    }

    public User(@NonNull String name, @NonNull String number, String email) {

        this.name = name;
        this.number = number;
        this.email = email;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
