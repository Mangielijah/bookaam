package com.omenacle.bookaam;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.omenacle.bookaam.DataClasses.Ticket;
import com.omenacle.bookaam.DataClasses.User;

@Database(entities = {Ticket.class, User.class}, version = 1, exportSchema = false)
public abstract class TicketRoomDatabase extends RoomDatabase {
    public abstract TicketDao ticketDao();
    public abstract UserDao userDao();

    private static volatile TicketRoomDatabase INSTANCE;

    static TicketRoomDatabase getDatabase(final Context context){
        if (INSTANCE  == null){
            synchronized (TicketRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TicketRoomDatabase.class, "bookaam_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
