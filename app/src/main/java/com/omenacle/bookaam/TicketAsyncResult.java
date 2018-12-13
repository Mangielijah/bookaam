package com.omenacle.bookaam;

import com.omenacle.bookaam.DataClasses.Ticket;

import java.util.List;

public interface TicketAsyncResult {
    void ticketAsyncFinished(List<Ticket> result);
}
