package com.omenacle.bookaam;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {


    public HomeFragment() {
        // Required empty public constructor
    }
    private Button mBookBus, mMyTicket, mHistory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        RelativeLayout homeLayout = view.findViewById(R.id.homeFragment);
        Animation slideRightToLeft = AnimationUtils.loadAnimation(getContext(), R.anim.slide_right_to_left);
        homeLayout.startAnimation(slideRightToLeft);
        //Buttons
        mBookBus = view.findViewById(R.id.home_book_bus_button);
        mMyTicket = view.findViewById(R.id.btn_my_tickets);
        mHistory = view.findViewById(R.id.btn_history);

        //Setting click listeners to buttons
        mBookBus.setOnClickListener(this);
        mHistory.setOnClickListener(this);
        mMyTicket.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_book_bus_button:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = getLayoutInflater().inflate(R.layout.bus_type_dialog, null);
                Button btnClassic = view.findViewById(R.id.classic_btn);
                Button btnVip = view.findViewById(R.id.vip_btn);

                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();

                btnClassic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        BookTicketFragment bookTicketFragment = new BookTicketFragment();
                        setFragment(bookTicketFragment);
                    }
                });
                btnVip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        BookVIPTicketFragment bookVIPTicketFragment = new BookVIPTicketFragment();
                        setFragment(bookVIPTicketFragment);
                    }
                });
                break;
            case R.id.btn_my_tickets:
                MyTicketsFragment myTicketsFragment = new MyTicketsFragment();
                setFragment(myTicketsFragment);
                break;
            case R.id.btn_history:
                Toast.makeText(getContext(), R.string.history, Toast.LENGTH_SHORT).show();
                break;

        }
    }


    private void setFragment(android.support.v4.app.Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_panel, fragment).addToBackStack("tag");
        fragmentTransaction.commit();
    }
}
