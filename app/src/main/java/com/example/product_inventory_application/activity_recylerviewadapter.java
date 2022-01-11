package com.example.product_inventory_application;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class activity_recylerviewadapter extends RecyclerView.Adapter<activity_recylerviewadapter.ViewHolder> {

    private ArrayList<Product> products;
    int images[];
    private LayoutInflater mInflater;
    Context context;

    // data is passed into the constructor
    public activity_recylerviewadapter(Context context, ArrayList<Product> product, int images[]) {
        this.context = context;
        this.products = product;
        this.images = images;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.cardview_warehouse, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Product exampleproduct = products.get(position);
        int image = images[position];
        holder.id.setText(""+exampleproduct.getId ());
        holder.name.setText(""+exampleproduct.getName());
        holder.quantity.setText(""+exampleproduct.getQuantity ());
        holder.warehouseid.setText (""+exampleproduct.getWarehouseid ());
        holder.img.setImageBitmap (BitmapFactory.decodeByteArray (exampleproduct.getImage (),0,exampleproduct.getImage ().length));
        holder.constraintLayout.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent (context, activity_productdescription.class);
                intent.putExtra ("id"           ,exampleproduct.getId ());
                context.startActivity (intent);
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount(){
        return products.size(); }

    public ArrayList<Product> getProducts(){
        return products;}


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,id,quantity, warehouseid;
        ImageView img;
        ConstraintLayout constraintLayout;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_cardview_itemname);
            id = itemView.findViewById(R.id.txt_cardview_itemid);
            quantity = itemView.findViewById(R.id.txt_cardviewhistory_quantity);
            img = itemView.findViewById (R.id.imageView_cardview);
            warehouseid = itemView.findViewById (R.id.txt_cardviewhistory_time);
            constraintLayout = itemView.findViewById (R.id.constraintlayout_card);

        }
    }
    public void filterList(ArrayList<Product> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        products = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }
}
