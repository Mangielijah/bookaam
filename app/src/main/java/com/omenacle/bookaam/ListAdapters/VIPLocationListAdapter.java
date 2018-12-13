package com.omenacle.bookaam.ListAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.omenacle.bookaam.BookingFragment.VIPLocationFragment;
import com.omenacle.bookaam.DataClasses.Route;
import com.omenacle.bookaam.R;

import java.util.List;

public class VIPLocationListAdapter extends RecyclerView.Adapter<VIPLocationListAdapter.LocationHolder> {
    private List<Route> mRouteList;
    private Context context;
    private RecyclerView mRecyclerView;
    VIPLocationFragment.OnLocationListener onLocationListener;

    public VIPLocationListAdapter(List<Route> routeList, RecyclerView locationRecyclerView, VIPLocationFragment.OnLocationListener onLocationListener) {
        mRouteList = routeList;
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

    @Override
    public void onBindViewHolder(@NonNull LocationHolder holder, int position) {
        holder.mLocation.setText(mRouteList.get(position).getRoute());
        holder.fare.setText(String.valueOf(mRouteList.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return mRouteList.size();
    }

    public class LocationHolder extends RecyclerView.ViewHolder {
        TextView mLocation, fare;
        public LocationHolder(View itemView) {
            super(itemView);
            mLocation = itemView.findViewById(R.id.from_to_location);
            fare = itemView.findViewById(R.id.fare);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = mRecyclerView.getChildLayoutPosition(v);
                    String location = mRouteList.get(position).getRoute();
                    String travel_time =  mRouteList.get(position).getTravel_time();
                    long price = mRouteList.get(position).getPrice();
                    onLocationListener.onSendLocation(location, price, travel_time);
                    Toast.makeText(context, location, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
