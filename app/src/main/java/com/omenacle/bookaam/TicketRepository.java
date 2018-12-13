package com.omenacle.bookaam;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.omenacle.bookaam.DataClasses.Ticket;

import java.util.List;

public class TicketRepository implements TicketAsyncResult {
    private TicketDao ticketDao;
    private MutableLiveData<List<Ticket>> allTickets;

    public TicketRepository(Application application) {
        TicketRoomDatabase db = TicketRoomDatabase.getDatabase(application);
        ticketDao = db.ticketDao();
        allTickets = new MutableLiveData<>();
    }

    @Override
    public void ticketAsyncFinished(List<Ticket> result) {
        allTickets.setValue(result);
    }

    public void insert(Ticket ticket){
        new insertTicketAsyncTask(ticketDao).execute(ticket);
    }

    public void update(Ticket ticket){
        new updateAsyncTask(ticketDao).execute(ticket);
    }

    public void getAllByDate(String date){
        queryAsyncTask task = new queryAsyncTask(ticketDao);
        task.ticketRepository = this;
        task.execute(date);
    }

    public MutableLiveData<List<Ticket>> getSearchResults(){
        return allTickets;
    }

    private static class insertTicketAsyncTask extends AsyncTask<Ticket, Void, Void> {

        private TicketDao mAsyncTicketDao;

        public insertTicketAsyncTask(TicketDao ticketDao){
            mAsyncTicketDao = ticketDao;
        }

        @Override
        protected Void doInBackground(Ticket... tickets) {
            mAsyncTicketDao.insert(tickets[0]);
            return null;
        }
    }

    private static class queryAsyncTask extends AsyncTask<String, Void, List<Ticket>>{

        private TicketDao mAsyncTicketDao;
        private TicketRepository ticketRepository = null;

        public queryAsyncTask(TicketDao dao) {
            this.mAsyncTicketDao = dao;
        }

        @Override
        protected List<Ticket> doInBackground(String... strings) {
            return mAsyncTicketDao.getAllByDate(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Ticket> tickets) {
            ticketRepository.ticketAsyncFinished(tickets);
        }
    }

    private static class updateAsyncTask extends AsyncTask<Ticket, Void, Void>{

        private TicketDao mAsyncTicketDao;

        public updateAsyncTask(TicketDao mAsyncTicketDao) {
            this.mAsyncTicketDao = mAsyncTicketDao;
        }

        @Override
        protected Void doInBackground(Ticket... tickets) {
            mAsyncTicketDao.update(tickets[0]);
            return null;
        }
    }
}
