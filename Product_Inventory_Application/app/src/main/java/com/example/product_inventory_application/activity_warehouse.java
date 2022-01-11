package com.example.product_inventory_application;


import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class activity_warehouse extends AppCompatActivity implements activity_recylerviewadapter.ItemClickListener {

    activity_recylerviewadapter adapter;
    Button btn_openadditem, btn_openmap;
    ImageView btn_backtohome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);

        btn_openadditem = findViewById (R.id.btn_add);
        btn_openmap = findViewById (R.id.btn_map);
        btn_backtohome = findViewById (R.id.btn_backtohomefromwarehouse);

        btn_backtohome.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                startActivity (new Intent (activity_warehouse.this,activity_home.class));
            }
        });

        btn_openmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (activity_warehouse.this, activity_map.class));
            }
        });

        btn_openadditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_warehouse.this, activity_additem.class));
            }
        });
        // data to populate the RecyclerView with
        ArrayList<Product> products = new ArrayList<>();

        Connection connection = SQLConnection.CONN();
        Statement statement = null;

        if(connection != null) {
            try {
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM WarehouseItem");
                while(resultSet.next()){
                    products.add(new Product(resultSet.getString(3), resultSet.getInt(1),
                            0, resultSet.getInt(2), resultSet.getString(5),
                            resultSet.getString(4), resultSet.getDouble(6), resultSet.getDouble(7), resultSet.getDouble(8)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }



        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new activity_recylerviewadapter(this, products);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}