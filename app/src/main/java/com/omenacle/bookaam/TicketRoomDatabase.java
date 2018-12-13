package com.omenacle.bookaam;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.omenacle.bookaam.DataClasses.Ticket;

@Database(entities = {Ticket.class}, version = 1, exportSchema = false)
public abstract class TicketRoomDatabase extends RoomDatabase {
    public abstract TicketDao ticketDao();

    private static volatile TicketRoomDatabase INSTANCE;

    static TicketRoomDatabase getDatabase(final Context context){
        if (INSTANCE  == null){
            synchronized (TicketRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TicketRoomDatabase.class, "ticket_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
