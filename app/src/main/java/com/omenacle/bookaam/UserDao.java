package com.omenacle.bookaam;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.omenacle.bookaam.DataClasses.User;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM user WHERE email = :email")
    User getUser(String email);

    @Update
    void update(User user);
}
