package com.omenacle.bookaam;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.omenacle.bookaam.DataClasses.Ticket;
import com.omenacle.bookaam.DataClasses.User;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private User userResult;


    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        userResult = userRepository.getSearchResult();
    }

   public User getSearchResults(){
        return userResult;
    }

    public void insert(User user){
        userRepository.insert(user);
    }

    public void  getUser(String email){
        userRepository.getUser(email);
    }
}
