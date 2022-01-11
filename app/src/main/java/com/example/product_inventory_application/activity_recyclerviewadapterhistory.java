package com.example.product_inventory_application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class activity_recyclerviewadapterhistory extends RecyclerView.Adapter<activity_recyclerviewadapterhistory.ViewHolder> {
    private ArrayList<Event> events;
    private LayoutInflater mInflater;
    Context context;

    // data is passed into the constructor
    public activity_recyclerviewadapterhistory(Context context, ArrayList<Event> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.cardview_history, parent, false);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder (@NonNull activity_recyclerviewadapterhistory.ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.action.setText ("  Action:"+event.getAction ());
        holder.name.setText ("  Item:"+event.getName ());
        holder.time.setText ("  "+event.getTime ());
        holder.user.setText ("  User:"+event.getUser ());

    }

    @Override
    public int getItemCount () {
        return events.size (); }

    public ArrayList<Event> getEvents(){
        return events;}

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, action, time, user;

        ViewHolder (View view) {
            super (view);
            name = view.findViewById (R.id.txt_cardviewhistory_name);
            time = view.findViewById (R.id.txt_cardviewhistory_time);
            action = view.findViewById (R.id.txt_cardviewhistory_action);
            user = view.findViewById (R.id.txt_cardviewhistory_user);
        }
    }

    public void filterList(ArrayList<Event> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        events = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }
}
