package com.omenacle.bookaam.BookingFragment;


import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.omenacle.bookaam.DataClasses.BookaamInfo;
import com.omenacle.bookaam.OnGetFirebaseDataListener;
import com.omenacle.bookaam.R;
import com.omenacle.bookaam.UserViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.omenacle.bookaam.BookingFragment.UserInfoFragment.TICKET_ID_NUM;
import static com.omenacle.bookaam.BookingFragment.UserInfoFragment.TICKET_NAME;
import static com.omenacle.bookaam.BookingFragment.UserInfoFragment.TICKET_NUMBER;


/**
 * A simple {@link Fragment} subclass.
 */
public class  VIPUserInfoFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    public VIPUserInfoFragment() {
        // Required empty public constructor
    }

    private OnUserInfoListener listener;
    private DatabaseReference mDatabase;
    String mName, mPhone, mId;
    TextInputEditText nameEditText, phoneEditText, idEditText;
    Button mSubmitButton;
    Context ctx;
    public static String TRAVEL_TIME = "TRAVEL_TIME";
    static String traveltime;
    String time, day;
    //bookam info
    String bEmail, bPass;
    Long bCharge;
    UserViewModel userViewModel;

    private SharedPreferences pref;



    public static VIPUserInfoFragment newInstance(String travel_time) {
        VIPUserInfoFragment fragment = new VIPUserInfoFragment();
        traveltime = travel_time;
        Bundle args = new Bundle();
        args.putString(TRAVEL_TIME, travel_time);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle mBundle = this.getArguments();
        if(mBundle != null){
            traveltime = mBundle.getString(TRAVEL_TIME);
        }

        pref = getActivity().getSharedPreferences("ticket", Context.MODE_PRIVATE);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("b").keepSynced(true);

        getBookamInfo(new OnGetFirebaseDataListener() {
            @Override
            public void onStart() {
                Log.d("Firebase", "Getting bookaam info from firebase");
            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Log.d("Firebase:", dataSnapshot.toString());
                BookaamInfo mBookaam = dataSnapshot.getValue(BookaamInfo.class);
                if (mBookaam != null){
                    bCharge = mBookaam.getVc();
                    bEmail = mBookaam.getE();
                    bPass = mBookaam.getP();
                }

            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("Firebase", errorMessage);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_user_info, container, false);

        nameEditText = view.findViewById(R.id.passenger_name);
        phoneEditText = view.findViewById(R.id.passenger_number);
        idEditText = view.findViewById(R.id.passenger_id);
        mSubmitButton = view.findViewById(R.id.btnSubmit);

        if(pref.contains(TICKET_NAME)){
            nameEditText.setText(pref.getString(TICKET_NAME, null));
        }
        if(pref.contains(TICKET_NUMBER)){
            phoneEditText.setText(pref.getString(TICKET_NUMBER, null));
        }
        if (pref.contains(TICKET_ID_NUM) && !pref.getString(TICKET_ID_NUM, null).equals("1") ){
            idEditText.setText(pref.getString(TICKET_ID_NUM, null));
        }

        ctx = getContext();


        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nameEditText.getText().toString().isEmpty()){
                    if (!phoneEditText.getText().toString().isEmpty()){
                        mName = nameEditText.getText().toString();
                        mPhone = phoneEditText.getText().toString();
                        mId = idEditText.getText().toString();

                        if(mId.isEmpty()){
                            mId = "1";
                        }


                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString(TICKET_NAME, mName);
                        editor.putString(TICKET_NUMBER, mPhone);
                        editor.putString(TICKET_ID_NUM, mId);
                        editor.apply();

                        if(traveltime != null)
                        {
                            List<String> timeArray = getTimeArray(traveltime);

                            AlertDialog.Builder timeDayDialog = new AlertDialog.Builder(ctx);
                            final View dialogView = getLayoutInflater().inflate(R.layout.time_day_dialog, null);
                            Button btnSubmitDialog = dialogView.findViewById(R.id.btn_submit);

                            Spinner travel_time = dialogView.findViewById(R.id.time_spinner);
                            Spinner travel_day = dialogView.findViewById(R.id.day_spinner);

                            //initialising spinner
                            ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(ctx, R.layout.spinner_row, timeArray);
                            ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(ctx, R.array.days_array, R.layout.spinner_row);

                            timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            travel_time.setOnItemSelectedListener(VIPUserInfoFragment.this);
                            travel_day.setOnItemSelectedListener(VIPUserInfoFragment.this);

                            //Setting adapter to spinner
                            travel_time.setAdapter(timeAdapter);
                            travel_day.setAdapter(dayAdapter);

                            timeDayDialog.setView(dialogView);
                            final AlertDialog dialog = timeDayDialog.create();
                            dialog.show();

                            btnSubmitDialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    listener.onSendUserInfo(mName, mPhone, mId, time, day, bCharge, bEmail, bPass);
                                }
                            });

                        }
                        else{
                            Snackbar routeSnackBar = Snackbar.make(view, R.string.no_route, Snackbar.LENGTH_LONG);
                            routeSnackBar.show();
                        }


                    }else{
                        Toast.makeText(getContext(), "Please fill in phone number", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "Please fill in name", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        Log.d("ViewId", String.valueOf(parent.getId()));
        switch (parent.getId()){
            case R.id.day_spinner:
                day = parent.getItemAtPosition(pos).toString();
                break;
            case R.id.time_spinner:
                time = parent.getItemAtPosition(pos).toString();
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface OnUserInfoListener{
        void onSendUserInfo(String name, String number, String NId, String time, String day, long bookaamC, String bookaamE, String bookaamP);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnUserInfoListener){
            listener = (OnUserInfoListener) context;
        }else{
            throw new RuntimeException(context.toString()
                + " must implement OnUserInfoListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private List<String> getTimeArray(String time){
        List<String> timeArray = new ArrayList<>();
        if(time != null && time.contains("_")){
            String s[] = time.split("_");
            if(s.length > 0){
                Log.d("SplitTime1", s[0]);
                Log.d("SplitTime1", s[1]);
                timeArray.addAll(Arrays.asList(s));
            }
        }
        return timeArray;
    }

    void getBookamInfo(final OnGetFirebaseDataListener listener){
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
        mDatabase.child("b").child("bookaam").addListenerForSingleValueEvent(mGetRouteListListener);
    }
}
