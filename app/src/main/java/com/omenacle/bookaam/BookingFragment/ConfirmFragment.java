package com.omenacle.bookaam.BookingFragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.omenacle.bookaam.Account.LoginActivity;
import com.omenacle.bookaam.DataClasses.BranchInfo;
import com.omenacle.bookaam.DataClasses.Ticket;
import com.omenacle.bookaam.MyTicketViewModel;
import com.omenacle.bookaam.OnGetFirebaseDataListener;
import com.omenacle.bookaam.OnPaymentMade;
import com.omenacle.bookaam.PaymentTask;
import com.omenacle.bookaam.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.omenacle.bookaam.ListAdapters.AgencyListAdapter.AGENCY_KEY;
import static com.omenacle.bookaam.ListAdapters.AgencyListAdapter.AGENCY_NAME;
import static com.omenacle.bookaam.BookingActivity.PRICE;
import static com.omenacle.bookaam.BookingActivity.ROUTE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmFragment extends Fragment {


    private static String agency_name, agency_key, p_name, p_num, p_id, troute, email, pass, travelTime, travelDay;
    private static long tprice, tcharge, num;
    private String branchEmail, branchPass;
    private long branchNum;

    @SuppressLint("StaticFieldLeak")
    private static Context ctx;
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
    private static String TICKET_CODE = "CLASSIC_TICKET_CODE";
    private String fareUrlRequest, chargeUrlRequest;
    String ticketCode;
    private SharedPreferences pref;

    private MyTicketViewModel myTicketViewModel;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myTicketViewModel = ViewModelProviders.of(this).get(MyTicketViewModel.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("OnCreate", "On Create ConfirmFragment");
        ctx =  getContext();

        //Ticket shared preference to store temporal ticket code to be used during transaction
        pref = getActivity().getSharedPreferences("ticket", Context.MODE_PRIVATE);

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
                FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                if(mCurrentUser != null){
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
                            if(!TextUtils.isEmpty(mNumber.getText().toString()))
                            {
                                String momoNumber = mNumber.getText().toString();

                                //Payment of platform charge url
                                //chargeUrlRequest = paymentUrl(tcharge, momoNumber, pass, email);
                                chargeUrlRequest = paymentUrl(1, momoNumber, pass, email);
                                //Payment of ticket fare url
                                //fareUrlRequest = paymentUrl(tprice, momoNumber, branchPass, branchEmail);
                                fareUrlRequest = paymentUrl(1, momoNumber, branchPass, branchEmail);
                                //Make ticket charge payment
                                final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                                builder.setTitle(getResources().getString(R.string.disclaimer));
                                builder.setMessage(getResources().getString(R.string.disclaimer_text)).

                                        setPositiveButton("I ACCEPT", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialog.dismiss();
                                                if (pref.contains(TICKET_CODE)){
                                                    //Process transport payment here
                                                    ticketCode = pref.getString(TICKET_CODE, null);
                                                    processTranPayment(fareUrlRequest, ticketCode);
                                                } else {
                                                    //Process fare payment first
                                                    PaymentTask chargeTask = new PaymentTask(ctx, chargeUrlRequest, "Platform Charge", new OnPaymentMade() {
                                                        @Override
                                                        public void onCompleted(String response, String msg) {
                                                            Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
                                                            Log.d("PaymentTask", response);
                                                            try {
                                                                JSONObject json = new JSONObject(response);
                                                                ticketCode = json.getString("TransactionID");
                                                                String status = json.getString("StatusCode");
                                                                Log.d("PaymentTaskStatus", status);
                                                                if(status.equals("01")){
                                                                    SharedPreferences.Editor editor = pref.edit();
                                                                    editor.putString(TICKET_CODE, ticketCode);
                                                                    editor.apply();

                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                                                                    builder .setMessage(ctx.getResources().getString(R.string.trans_dialog))
                                                                            .setPositiveButton(getResources().getString(R.string.continu), new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    dialogInterface.dismiss();
                                                                                    processTranPayment(fareUrlRequest, ticketCode);
                                                                                }
                                                                            })
                                                                            .setCancelable(false);
                                                                    AlertDialog alertDialog = builder.create();
                                                                    alertDialog.show();

                                                                }
                                                                else {
                                                                    Toast.makeText(ctx, "Transaction Error", Toast.LENGTH_SHORT).show();
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(String msg) {
                                                            Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                                    chargeTask.execute();
                                                }

                                            }
                                        }).setNegativeButton("DECLINE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialog.dismiss();
                                    }
                                });

                                final AlertDialog dialog = builder.create();
                                dialog.show();

                            }
                            else
                            {
                                Toast.makeText(ctx, getResources().getText(R.string.momonumber_error), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }

            }
        });

        return view;
    }

    private void processTranPayment(String fareUrlRequest, final String ticketCode) {
        PaymentTask fareTask = new PaymentTask(ctx, fareUrlRequest, "Transport Fare", new OnPaymentMade() {
            @Override
            public void onCompleted(String response, String msg) {
                Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                    String status = json.getString("StatusCode");
                    Log.d("PaymentTaskStatus", status);
                    if(status.equals("01")){
                        pd = new ProgressDialog(ctx);
                        pd.setTitle(getResources().getString(R.string.saving));
                        pd.setMessage(getResources().getString(R.string.please_wait));
                        pd.setCancelable(true);
                        pd.setCanceledOnTouchOutside(false);
                        pd.show();

                        Ticket mTicket = new Ticket(agency_key, getBranchKey(agency_key, troute), Long.parseLong(ticketCode), travelDay, p_name, Long.parseLong(p_num), Long.parseLong(p_id), troute, travelTime, "n");

                        //Sending ticket online to firebase
                        sendTicketOnline(mTicket);


                        //deleting ticket code from shared preferences
                        SharedPreferences.Editor editor = pref.edit();
                        editor.remove(TICKET_CODE).apply();

                    }
                    else {
                        Toast.makeText(ctx, "Transaction Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
            }
        });
        fareTask.execute();
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
       travelTime = time;

        nameTextView.setText(pName);
        numberTextView.setText(pNum);
        if(pId.equals("1")){
            idTextView.setText(ctx.getResources().getText(R.string.no_idcard));
        }else{
            idTextView.setText(pId);
        }
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
            travelDay = formattedDateToday;
            dateTravelTextView.setText(formattedDateToday);
        } else if(day.toLowerCase().equals("tomorrow")){
            travelDay = formattedDateTom;
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
        if(route == null){
            Toast.makeText(ctx, getResources().getString(R.string.no_route_selected), Toast.LENGTH_SHORT).show();
            return "";
        }
        else{

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
    }


    //This function returns a request url of the payment
    private String paymentUrl(long amount, String number, String p, String e){

        return "https://developer.mtn.cm/OnlineMomoWeb/faces/transaction/transactionRequest.xhtml" +
                "?idbouton=2&typebouton=PAIE&_amount=" + amount + "&_tel=" + number + "&_clP=" + p +
                "&_email=" + e + "&submit.x=104&submit.y=70";

    }

    private void sendTicketOnline(final Ticket mTicket){
        Log.d("Ticket:", mTicket.toString());
        final ProgressDialog pd = new ProgressDialog(ctx);
        pd.setMessage(ctx.getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        mDatabase.child("t").child(ticketCode).setValue(mTicket).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                //Sending to local storage using room database
                try{
                    myTicketViewModel.insert(mTicket);
                }catch (Exception e){
                    Log.d("Insert", "Error Inserting ticket: "+e.getMessage());
                }
                pd.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder .setMessage(ctx.getResources().getString(R.string.completed_text))
                        .setTitle(ctx.getResources().getString(R.string.completed));
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

}
