package com.example.product_inventory_application;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class activity_recylerviewadapter extends RecyclerView.Adapter<activity_recylerviewadapter.ViewHolder> {

    private ArrayList<Product> products;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public activity_recylerviewadapter(Context context, ArrayList<Product> product) {
        this.mInflater = LayoutInflater.from(context);
        this.products = product;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.id.setText(""+products.get(position).getId());
        holder.name.setText(""+products.get(position).getName());
        holder.quantity.setText(""+products.get(position) .getWeight());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return products.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name,id,quantity;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_itemname);
            id = itemView.findViewById(R.id.txt_itemid);
            quantity = itemView.findViewById(R.id.txt_itemquantity);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Product getItem(int position) {
        return products.get(position);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
