package com.omenacle.bookaam.BookingFragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.omenacle.bookaam.DataClasses.BranchInfo;
import com.omenacle.bookaam.OnGetFirebaseDataListener;
import com.omenacle.bookaam.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Timeout;

import static com.omenacle.bookaam.ListAdapters.AgencyListAdapter.AGENCY_KEY;
import static com.omenacle.bookaam.ListAdapters.AgencyListAdapter.AGENCY_NAME;
import static com.omenacle.bookaam.BookingActivity.PRICE;
import static com.omenacle.bookaam.BookingActivity.ROUTE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmFragment extends Fragment {


    public static String agency_name, agency_key, p_name, p_num, p_id, troute, email, pass;
    public static long tprice, tcharge, num;
    public String branchEmail, branchPass;
    public long branchNum;

    Context ctx;
    ProgressDialog pd;

    private OnConfirmTicketListener listener;
    private DatabaseReference mDatabase;

    Button btnPay;

    private static AppCompatTextView nameTextView;
    private static AppCompatTextView numberTextView;
    private static AppCompatTextView routeTextView;
    private static AppCompatTextView idTextView;
    private static AppCompatTextView agencyTextView;
    private static AppCompatTextView timeTextView;
    private static AppCompatTextView dateTextView;
    private static AppCompatTextView dateTravelTextView;
    private static AppCompatTextView priceTextView;
    private static AppCompatTextView chargeTextView;

    Request fareUrlRequest, chargeUrlRequest;

    public ConfirmFragment() {
        // Required empty public constructor
    }

    public static ConfirmFragment newInstance(String agencyName, String agencyKey, String route, long price) {
        ConfirmFragment fragment = new ConfirmFragment();
        Bundle args = new Bundle();
        args.putString(AGENCY_NAME, agencyName);
        args.putString(AGENCY_KEY, agencyKey);
        args.putLong(PRICE, price);
        args.putString(ROUTE, route);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("OnCreate", "On Create ConfirmFragment");
        ctx =  getContext();
        Bundle mBundle = this.getArguments();
        if(mBundle != null){
            agency_name = mBundle.getString(AGENCY_NAME);
            agency_key = mBundle.getString(AGENCY_KEY);
            troute = mBundle.getString(ROUTE);
            tprice = mBundle.getLong(PRICE);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        getBranchInfo(new OnGetFirebaseDataListener() {
            @Override
            public void onStart() {
                Log.d("Firebase:", "Getting branch payment info");
            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                Log.d("Firebase:", dataSnapshot.toString());
                BranchInfo mBranch = dataSnapshot.getValue(BranchInfo.class);
                if (mBranch != null){
                    branchEmail = mBranch.getE();
                    branchPass = mBranch.getP();
                    branchNum = mBranch.getN();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("Firebase", errorMessage);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Pause", "OnPause ConfirmFragment");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm, container, false);

        Log.d("OncreateView", "Oncreate view ConfirmFragment");

        btnPay = view.findViewById(R.id.btn_pay);

        nameTextView = view.findViewById(R.id.name_ticket_holder);
        numberTextView = view.findViewById(R.id.holder_number);
        idTextView = view.findViewById(R.id.holder_nid);
        agencyTextView = view.findViewById(R.id.agency_booked);
        dateTextView = view.findViewById(R.id.date);
        dateTravelTextView = view.findViewById(R.id.departure_date);
        timeTextView = view.findViewById(R.id.departure_time);
        priceTextView = view.findViewById(R.id.ticket_price);
        routeTextView = view.findViewById(R.id.ticket_location);
        chargeTextView = view.findViewById(R.id.charge);


        routeTextView.setText(troute);
        agencyTextView.setText(agency_name);
        priceTextView.setText(String.valueOf(tprice) + "FCFA");


        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dBuilder = new AlertDialog.Builder(getContext());
                final View dialogView = getLayoutInflater().inflate(R.layout.payment_dialog, null);
                final TextInputEditText mNumber = dialogView.findViewById(R.id.momo_number);
                Button payButton = dialogView.findViewById(R.id.btn_pay);
                dBuilder.setView(dialogView);
                final AlertDialog dialog = dBuilder.create();
                dialog.show();
                payButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String momoNumber = mNumber.getText().toString();

                        //Payment of platform charge url
                        //chargeUrlRequest = paymentUrl(tcharge, momoNumber, pass, email);
                        chargeUrlRequest = paymentUrl(5, momoNumber, pass, email);
                        //Payment of ticket fare url
                        fareUrlRequest = paymentUrl(tprice, momoNumber, branchPass, branchEmail);
                        dialog.dismiss();
                        pd = new ProgressDialog(ctx);
                        pd.setTitle("Processing");
                        pd.setMessage("Making platform charge payment\nDial *126# and confirm");
                        pd.setCanceledOnTouchOutside(false);
                        pd.setCancelable(false);
                        pd.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                makePayment(chargeUrlRequest, "Platform charge");
                            }
                        }).start();
                    }
                });
            }
        });

        return view;
    }

    public interface OnConfirmTicketListener{
        void onBookTicket(String ticketCode);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnConfirmTicketListener){
            listener = (OnConfirmTicketListener) context;
        }else{
            throw new RuntimeException(context.toString()
                + " must implement OnConfirmTicketListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @SuppressLint("SetTextI18n")
    public static void updateConfirmFragment(String pName, String pNum, String pId, String time, String day, long bCharge, long bNum, String bEmail, String bPass) {
       p_name = pName;
       p_num = pNum;
       p_id = pId;
       tcharge = bCharge;
       num = bNum;
       email = bEmail;
       pass = bPass;
        nameTextView.setText(pName);
        numberTextView.setText(pNum);
        idTextView.setText(pId);
        chargeTextView.setText(String.valueOf(bCharge) + "FCFA");
        timeTextView.setText(time);

        Calendar calendar = Calendar.getInstance();

        Log.d("Time c: ", day + ": "+ calendar.getTime().toString());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDateToday = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        String formattedDateTom = dateFormat.format(calendar.getTime());
        Log.d("Date: ", formattedDateTom);

        if (day.toLowerCase().equals("today")) {
            dateTravelTextView.setText(formattedDateToday);
        } else if(day.toLowerCase().equals("tomorrow")){
            dateTravelTextView.setText(formattedDateTom);
        }

        dateTextView.setText(formattedDateToday);
    }

    void getBranchInfo(final OnGetFirebaseDataListener listener){
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
        Log.d("Firebase: ", getBranchKey(agency_key, troute));
        mDatabase.child("b").child(getBranchKey(agency_key, troute)).addListenerForSingleValueEvent(mGetRouteListListener);
    }

    private String getBranchKey(String ak, String route){

        String location = null;
        if(route.contains("->")){
            String s[] = route.split("->");
            if(s.length > 0){
                Log.d("SplitTime1", s[0]);
                Log.d("SplitTime1", s[1]);
                location = s[0];
            }
        }
        return ak+""+location;
    }


    //This function returns a request(OkHTTP) of the payment
    private Request paymentUrl(long amount, String number, String p, String e){

        String url  = "http://developer.mtn.cm/OnlineMomoWeb/faces/transaction/transactionRequest.xhtml" +
                "?idbouton=2&typebouton=PAIE&_amount=" + amount + "&_tel=" + number + "&_clP=" + p +
                "&_email=" + e + "&submit.x=104&submit.y=70";


        Log.d("PaymentURL", url);

        return new Request.Builder()
                .url(url)
                .build();

    }

    private void makePayment(Request request, String reason){


        OkHttpClient client = new OkHttpClient();

        try {
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){

                Log.d("Response:", response.toString());
                pd.dismiss();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Could not complete payment");
                alertDialog.show();

            }else{

                final String responseData = response.body().string();
                JSONObject json = new JSONObject(responseData);
                Log.d("ResponseJson", json.toString());

            }
        } catch (IOException e) {
            Log.d("Error", e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
            e.printStackTrace();
        }

        // Get a handler that can be used to post to the main thread
        /*client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            pd.dismiss();
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
                            alertDialog.setTitle("Error");
                            alertDialog.setMessage("Could not complete payment");
                            alertDialog.show();
                        }catch (Exception e){
                            Log.d("ErrorUI", e.getMessage());
                        }
                    }
                });

                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }else{
                    Log.d("Response", response.body().string());
                    try {
                        String responseData = response.body().string();
                        JSONObject json = new JSONObject(responseData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                pd.setMessage("Done");
                            }catch (Exception e){
                                Log.d("ErrorUI", e.getMessage());
                            }
                        }
                    });
                }
            }
        });*/
    }
}
