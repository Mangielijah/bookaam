package com.omenacle.bookaam;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.ScrollView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyTicketsFragment extends Fragment {


    public MyTicketsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_tickets, container, false);

        ScrollView homeLayout = view.findViewById(R.id.myTicketFragment);
        Animation slideRightToLeft = AnimationUtils.loadAnimation(getContext(), R.anim.slide_right_to_left);
        homeLayout.startAnimation(slideRightToLeft);

        return view;
    }

}
