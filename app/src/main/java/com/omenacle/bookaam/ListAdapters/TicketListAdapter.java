package com.omenacle.bookaam.ListAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.omenacle.bookaam.DataClasses.Ticket;
import com.omenacle.bookaam.R;

import java.util.List;

public class TicketListAdapter extends RecyclerView.Adapter<TicketListAdapter.ViewHolder> {

    private List<Ticket> tickets;
    private Context ctx;
    private RecyclerView recyclerView;

    public void setTicketList(List<Ticket> ticketList){
        tickets = ticketList;
        notifyDataSetChanged();
    }

    public TicketListAdapter(RecyclerView recycler) {
        recyclerView = recycler;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        View v = LayoutInflater.from(ctx).inflate(R.layout.my_ticket_row_design, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.code.setText(ctx.getResources().getString(R.string.code)+": " +String.valueOf(tickets.get(position).getCode()));
        holder.agency.setText(ctx.getResources().getString(R.string.agency)+": "+tickets.get(position).getA());
        holder.name.setText(tickets.get(position).getName());
        holder.number.setText(String.valueOf(tickets.get(position).getNum()));
        holder.route.setText(tickets.get(position).getR());
        holder.date.setText(tickets.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return tickets == null ? 0 : tickets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {   Button button;
        TextView code, agency, name, number, route, date;

        public ViewHolder(final View itemView) {
            super(itemView);

            code = itemView.findViewById(R.id.ticketCode);
            agency = itemView.findViewById(R.id.agency);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            route = itemView.findViewById(R.id.route);
            date = itemView.findViewById(R.id.date);
            button = itemView.findViewById(R.id.btn_postpone);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = recyclerView.getChildLayoutPosition(itemView);
                    String code = String.valueOf(tickets.get(position).getCode());
                    Ticket ticket = tickets.get(position);
                    Toast.makeText(ctx, code, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
