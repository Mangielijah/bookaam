package com.omenacle.bookaam;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.omenacle.bookaam.DataClasses.Agency;
import com.omenacle.bookaam.ListAdapters.AgencyListAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookTicketFragment extends Fragment {


    public BookTicketFragment() {
        // Required empty public constructor
    }
    RecyclerView mAgencyRecyclerView;
    private DatabaseReference mDatabase;
    List<Agency> mBusAgencyList = new ArrayList<>();
    ProgressDialog pd;
    AgencyListAdapter mAgencyListAdapter = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("a").keepSynced(true);
        Log.d("onCreate", "Executing OnCreate Method");
            /*
                Getting agencies from Firebase online and adding too the
                Bus agency List (mBusAgencyList)

             */
        if(mBusAgencyList.isEmpty()){

            getAgencies(new OnGetFirebaseDataListener() {
                @Override
                public void onStart() {
                    pd = new ProgressDialog(getContext());
                    pd.setTitle(getResources().getString(R.string.fetching_agencies_available));
                    pd.setMessage(getResources().getString(R.string.please_wait));
                    pd.setCancelable(true);
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();
                }

                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    //Log.d("GetAgencyTask", dataSnapshot.toString());
                    for(DataSnapshot agencySnapShot : dataSnapshot.getChildren()){
                        Agency mAgency = agencySnapShot.getValue(Agency.class);
                        if (mAgency != null) {
                            mBusAgencyList.add(mAgency);
                        }
                    }

                    Toast.makeText(getContext(), getResources().getString(R.string.select_agency_from_the_list), Toast.LENGTH_LONG).show();
                    //if Bus Agency List is not null
                    if (mBusAgencyList != null){
                        Log.d("BusList", mBusAgencyList.toString());
                        mAgencyListAdapter.notifyDataSetChanged();
                    }


                    pd.dismiss();

                    Animation slideRightToLeft = AnimationUtils.loadAnimation(getContext(), R.anim.slide_right_to_left);
                    mAgencyRecyclerView.startAnimation(slideRightToLeft);
                }


                @Override
                public void onFailure(String errorMessage) {
                    Log.d("Error", errorMessage);
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
            });

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Log.d("onCreateView", "Executing onCreateView Method");
        View view = inflater.inflate(R.layout.fragment_book_ticket, container, false);
        mAgencyRecyclerView = view.findViewById(R.id.agency_recycler_view);
        mAgencyRecyclerView.setHasFixedSize(true);
        mAgencyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAgencyListAdapter = new AgencyListAdapter(mBusAgencyList, mAgencyRecyclerView);
        mAgencyRecyclerView.setAdapter(mAgencyListAdapter);

        return view;
    }

    void getAgencies(final OnGetFirebaseDataListener listener){
        listener.onStart();
        ValueEventListener mAgencyListListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("GetAgencyTask", databaseError.getMessage());
                listener.onFailure(databaseError.getMessage());
            }
        };

        mDatabase.child("a").addValueEventListener(mAgencyListListener);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

}
