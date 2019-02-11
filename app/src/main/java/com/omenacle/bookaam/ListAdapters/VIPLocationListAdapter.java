package com.omenacle.bookaam.ListAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.omenacle.bookaam.BookingFragment.VIPLocationFragment;
import com.omenacle.bookaam.DataClasses.Route;
import com.omenacle.bookaam.DataClasses.Seats;
import com.omenacle.bookaam.R;

import java.util.List;

public class VIPLocationListAdapter extends RecyclerView.Adapter<VIPLocationListAdapter.LocationHolder> {
    private List<Route> mRouteList;
    private List<Seats> mSeatList;
    private Context context;
    private RecyclerView mRecyclerView;
    VIPLocationFragment.OnLocationListener onLocationListener;

    public VIPLocationListAdapter(List<Route> routeList, List<Seats> mSeatList, RecyclerView locationRecyclerView, VIPLocationFragment.OnLocationListener onLocationListener) {
        mRouteList = routeList;
        this.mSeatList = mSeatList;
        mRecyclerView = locationRecyclerView;
        this.onLocationListener = onLocationListener;
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        CardView mLocationContainer = (CardView) LayoutInflater.from(context).inflate(R.layout.location_row_design, parent, false);
        return new LocationHolder(mLocationContainer);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LocationHolder holder, int position) {
        holder.mLocation.setText(mRouteList.get(position).getRoute());
        holder.fare.setText(String.valueOf(mRouteList.get(position).getPrice()) + "FCFA");
        holder.type.setText("VIP");
        if(!mSeatList.isEmpty())
        {
            Log.d("Test", position + " : "+ mSeatList.size());
            if(position >= mSeatList.size()){

                holder.seatLeft.setText(
                        "Morning: "+ 0
                                +" left\nAfternoon: "+ 0
                                +" left\nNight: "+ 0
                                +" left");
            }
            else
            {
                if (mRouteList.get(position).getR_k().equals(mSeatList.get(position).getR_k()))
                {
                    holder.seatLeft.setText(
                            "Morning: "+ mSeatList.get(position).getM()
                                    +" left\nAfternoon: "+ mSeatList.get(position).getA()
                                    +" left\nNight: "+ mSeatList.get(position).getN()
                                    +" left");

                }

            }

        }
    }

    @Override
    public int getItemCount() {
        return mRouteList.size();
    }

    public class LocationHolder extends RecyclerView.ViewHolder {
        TextView mLocation, fare, type, seatLeft;
        public LocationHolder(View itemView) {
            super(itemView);
            mLocation = itemView.findViewById(R.id.from_to_location);
            fare = itemView.findViewById(R.id.fare);
            type = itemView.findViewById(R.id.type);
            seatLeft = itemView.findViewById(R.id.seatLeft);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = mRecyclerView.getChildLayoutPosition(v);
                    String location = mRouteList.get(position).getRoute();
                    String travel_time =  mRouteList.get(position).getTravel_time();
                    long price = mRouteList.get(position).getPrice();
                    onLocationListener.onSendLocation(location, price, travel_time, mSeatList.get(position));
                    Toast.makeText(context, location, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
