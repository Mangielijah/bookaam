package com.omenacle.bookaam.BookingFragment;


import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.omenacle.bookaam.ListAdapters.LocationListAdapter;
import com.omenacle.bookaam.OnGetFirebaseDataListener;
import com.omenacle.bookaam.R;
import com.omenacle.bookaam.DataClasses.Route;

import java.util.ArrayList;
import java.util.List;

import static com.omenacle.bookaam.ListAdapters.AgencyListAdapter.AGENCY_KEY;
import static com.omenacle.bookaam.ListAdapters.AgencyListAdapter.AGENCY_NAME;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {


    public LocationFragment() {
        // Required empty public constructor
    }

    private DatabaseReference mDatabase;
    private OnLocationListener onLocationListener;
    String agency_name = "";
    String agency_key = "";
    RecyclerView locationRecyclerView;
    LocationListAdapter locationListAdapter;
    ProgressDialog pd;
    List<Route> mRouteList = new ArrayList<>();

    public static LocationFragment newInstance(String key, String name) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString(AGENCY_KEY, key);
        args.putString(AGENCY_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mBundle = this.getArguments();

        if(mBundle != null){
            agency_name = mBundle.getString(AGENCY_NAME);
            agency_key = mBundle.getString(AGENCY_KEY);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("r").keepSynced(true);
        if(mRouteList.isEmpty()){
        /*

            Getting the routes from firebase online and adding it
            to the list of routes

         */
            getRoutes(new OnGetFirebaseDataListener() {
                @Override
                public void onStart() {
                    pd = new ProgressDialog(getContext());
                    pd.setTitle(getResources().getString(R.string.fetching_routes_available));
                    pd.setMessage(getResources().getString(R.string.please_wait));
                    pd.setCancelable(true);
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();
                }

                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    for (DataSnapshot routeSnapshot : dataSnapshot.getChildren()){
                        Route mRoute = routeSnapshot.getValue(Route.class);
                        Log.d("LocationFragment", mRoute.toString());
                        if(mRoute != null){
                            if (mRoute.getA_k().equals(agency_key)){
                                mRouteList.add(mRoute);
                            }
                        }
                    }

                    Toast.makeText(getContext(),getResources().getString(R.string.select_route), Toast.LENGTH_LONG).show();
                    //If route list is not empty
                    if (mRouteList != null){
                        Log.d("RouteList", mRouteList.toString());
                        locationListAdapter.notifyDataSetChanged();
                    }

                    pd.dismiss();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.d("LocationError", errorMessage);
                    pd.dismiss();
                }
            });

        }
        else{

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_location, container, false);

        //textViewAgency.setText(agency_name);
        locationRecyclerView = view.findViewById(R.id.locationRecyclerView);
        locationRecyclerView.setHasFixedSize(true);
        locationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        locationListAdapter = new LocationListAdapter(mRouteList, locationRecyclerView, onLocationListener);
        locationRecyclerView.setAdapter(locationListAdapter);

        return view;
    }

    void getRoutes(final OnGetFirebaseDataListener listener){
        listener.onStart();
        ValueEventListener mGetRouteListListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure(databaseError.getMessage());
            }
        };
        mDatabase.child("r").orderByChild("a_k").equalTo(agency_key).addValueEventListener(mGetRouteListListener);
    }

    public interface OnLocationListener{
        void onSendLocation(String location, long price, String travel_time);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnLocationListener){
            onLocationListener = (OnLocationListener) context;
        }else{
            throw new RuntimeException(context.toString()
            + " must implement OnLocationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onLocationListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
