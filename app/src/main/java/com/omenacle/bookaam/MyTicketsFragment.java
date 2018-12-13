package com.omenacle.bookaam;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.omenacle.bookaam.DataClasses.Ticket;
import com.omenacle.bookaam.ListAdapters.TicketListAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyTicketsFragment extends Fragment {


    public MyTicketsFragment() {
        // Required empty public constructor
    }

    RecyclerView myTicketRecycler;
    TicketListAdapter adapter;
    MyTicketViewModel myTicketViewModel;
    TextView mTextView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myTicketViewModel = ViewModelProviders.of(this).get(MyTicketViewModel.class);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDateToday = dateFormat.format(calendar.getTime());

        myTicketViewModel.getAllByDate(formattedDateToday);


        myTicketViewModel.getSearchResults().observe(this, new Observer<List<Ticket>>() {
            @Override
            public void onChanged(@Nullable List<Ticket> tickets) {
                Log.d("Tickets", tickets.toString());
                adapter.setTicketList(tickets);

                if(tickets.size() < 1){
                    myTicketRecycler.setVisibility(View.GONE);
                    mTextView.setVisibility(View.VISIBLE);
                }else {
                    myTicketRecycler.setVisibility(View.VISIBLE);
                    mTextView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_tickets, container, false);

        RelativeLayout homeLayout = view.findViewById(R.id.myTicketFragment);
        Animation slideRightToLeft = AnimationUtils.loadAnimation(getContext(), R.anim.slide_right_to_left);
        homeLayout.startAnimation(slideRightToLeft);

        mTextView = view.findViewById(R.id.no_ticket);
        myTicketRecycler = view.findViewById(R.id.my_ticket_recycler);
        adapter = new TicketListAdapter(myTicketRecycler);
        myTicketRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        myTicketRecycler.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
