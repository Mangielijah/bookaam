package com.omenacle.bookaam;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.omenacle.bookaam.DataClasses.Ticket;

import java.util.List;

@Dao
public interface TicketDao {

    @Insert
    void insert(Ticket ticket);

    @Query("SELECT * FROM ticket WHERE date >= :date ORDER BY code ASC")
    List<Ticket> getAllByDate(String date);

    @Update
    void update(Ticket ticket);
}
