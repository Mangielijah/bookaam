package com.omenacle.bookaam;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public interface OnGetFirebaseDataListener {
    //call back methods
    void onStart();
    void onSuccess(DataSnapshot dataSnapshot);
    void onFailure(String errorMessage);
}
