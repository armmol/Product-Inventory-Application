package com.example.product_inventory_application;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SliderItemAdapter extends RecyclerView.Adapter<SliderItemAdapter.MyViewHolder> {

    List<Product> list;

    public SliderItemAdapter (List<Product> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.warehousemap_slideritem, parent, false));
    }

    @Override
    public void onBindViewHolder (@NonNull MyViewHolder holder, int position) {
        if(list.get(position).getImage ()==null)
        {
            holder.img.setImageResource (R.drawable.sadface);
            holder.txt.setText (list.get (position).getName ());
        }
        else {
            holder.img.setImageBitmap (BitmapFactory.decodeByteArray
                    (list.get (position).getImage (), 0, list.get (position).getImage ().length));
            holder.txt.setText (list.get (position).getName ());
        }
    }

    @Override
    public int getItemCount () {
        return list.size ();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.slideritem);
            txt = itemView.findViewById (R.id.txt_slideritem_itemname);
        }
    }
}
