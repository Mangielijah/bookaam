package com.omenacle.bookaam;

import android.app.Application;
import android.os.AsyncTask;

import com.omenacle.bookaam.DataClasses.Ticket;
import com.omenacle.bookaam.DataClasses.User;

public class UserRepository {
    private UserDao userDao;
    private User user;

    public UserRepository(Application application) {
        TicketRoomDatabase db = TicketRoomDatabase.getDatabase(application);
        userDao = db.userDao();
        user = new User();
    }


    //Insert Async Task
    private static class insertUserAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncUserDao;

        public insertUserAsyncTask(UserDao userDao){
            mAsyncUserDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            mAsyncUserDao.insert(users[0]);
            return null;
        }
    }

    //Search for user in db
    private static class queryUserAsyncTask extends AsyncTask<String, Void, User>{

        private UserDao mAsyncUserDao;
        private UserRepository userRepository = null;

        public queryUserAsyncTask(UserDao dao) {
            this.mAsyncUserDao = dao;
        }

        @Override
        protected User doInBackground(String... strings) {
            return mAsyncUserDao.getUser(strings[0]);
        }

        @Override
        protected void onPostExecute(User user) {
            userRepository.userAsyncFinished(user);
        }
    }


    //Update Async task
    private static class updateAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncUserDao;

        public updateAsyncTask(UserDao mAsyncTicketDao) {
            this.mAsyncUserDao = mAsyncUserDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            mAsyncUserDao.update(users[0]);
            return null;
        }
    }

    public void userAsyncFinished(User u){
        this.user.setEmail(u.getEmail());
        this.user.setId(u.getId());
        this.user.setName(u.getName());
        this.user.setNumber(u.getNumber());
    }

    public User getSearchResult(){
        return user;
    }

    public void insert(User user){
        new UserRepository.insertUserAsyncTask(userDao).execute(user);
    }

    public void update(Ticket ticket){
        new UserRepository.updateAsyncTask(userDao).execute(user);
    }

    public void getUser(String email){
        queryUserAsyncTask task = new queryUserAsyncTask(userDao);
        task.userRepository = this;
        task.execute(email);
    }
}
