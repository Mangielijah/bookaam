package com.omenacle.bookaam;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.omenacle.bookaam.DataClasses.Ticket;

import java.util.List;

public class MyTicketViewModel extends AndroidViewModel {

    private TicketRepository ticketRepository;
    private MutableLiveData<List<Ticket>> ticketResult;


    public MyTicketViewModel(@NonNull Application application) {
        super(application);
        ticketRepository = new TicketRepository(application);
        ticketResult = ticketRepository.getSearchResults();
    }

    MutableLiveData<List<Ticket>> getSearchResults(){
        return ticketResult;
    }

    public void insert(Ticket ticket){
        ticketRepository.insert(ticket);
    }

    public void  getAllByDate(String date){
        ticketRepository.getAllByDate(date);
    }
}
