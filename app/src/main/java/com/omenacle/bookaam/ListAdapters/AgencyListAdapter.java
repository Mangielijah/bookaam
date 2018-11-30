package com.omenacle.bookaam.ListAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.omenacle.bookaam.DataClasses.Agency;
import com.omenacle.bookaam.BookingActivity;
import com.omenacle.bookaam.R;

import java.util.List;

public class AgencyListAdapter extends RecyclerView.Adapter<AgencyListAdapter.AgencyHolder> {
    private List<Agency> mAgencies;
    private Context context;
    private RecyclerView mRecyclerView;
    public static String AGENCY_KEY = "AGENCY_KEY";
    public static String AGENCY_NAME = "AGENCY_NAME";

    public AgencyListAdapter(List<Agency> agencies, RecyclerView mAgencyRecyclerView) {
     mAgencies = agencies;
     mRecyclerView = mAgencyRecyclerView;
    }

    @NonNull
    @Override
    public AgencyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        CardView mAgencyCardView = (CardView) LayoutInflater.from(context).inflate(R.layout.bus_agency_row_design, parent, false);
        return new AgencyHolder(mAgencyCardView);
    }

    @Override
    public void onBindViewHolder(@NonNull AgencyHolder holder, int position) {
        holder.mAgencyTitle.setText(mAgencies.get(position).getA_n());
    }

    @Override
    public int getItemCount() {
        return mAgencies.size();
    }

    
    public class AgencyHolder extends RecyclerView.ViewHolder {
        TextView mAgencyTitle;
        RatingBar mAgencyRating;
        public AgencyHolder(View itemView) {
            super(itemView);
            mAgencyTitle = itemView.findViewById(R.id.agency_title);
            mAgencyRating = itemView.findViewById(R.id.agency_rating);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = mRecyclerView.getChildLayoutPosition(v);
                    String agencyKey = mAgencies.get(position).getA_k();
                    String agencyName = mAgencies.get(position).getA_n();
                    Intent startBooking = new Intent(context, BookingActivity.class);
                    startBooking.putExtra(AGENCY_KEY, agencyKey);
                    startBooking.putExtra(AGENCY_NAME, agencyName);
                    context.startActivity(startBooking);
                }
            });
        }
    }
}
