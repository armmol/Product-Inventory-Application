package com.example.product_inventory_application;


import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class activity_warehouse extends AppCompatActivity {

    ArrayList<Product> products = new ArrayList<>();
    activity_recylerviewadapter adapter;
    Button btn_openadditem, btn_openmap;
    ImageView btn_backtohome;
    RecyclerView recyclerView;
    SearchView searchView;
    Spinner SWarehouse;
    int[] images = {R.drawable.car1, R.drawable.car2
            , R.drawable.car3, R.drawable.car4
            , R.drawable.car5, R.drawable.car6
            , R.drawable.car7, R.drawable.car8
            , R.drawable.car9, R.drawable.car10};
    int warehousenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);

        btn_openadditem = findViewById(R.id.btn_Scrollview);
        btn_openmap = findViewById(R.id.btn_map);
        btn_backtohome = findViewById(R.id.btn_backtohomefromwarehouse);

        btn_backtohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_warehouse.this, activity_home.class));
            }
        });

        btn_openmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_warehouse.this, activity_map.class));
            }
        });

        btn_openadditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_warehouse.this, activity_warehousemap.class));
            }
        });
        // data to populate the RecyclerView with


        Connection connection = SQLConnection.CONN();
        Statement statement = null;


        if (connection != null) {
            try {
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM WarehouseItem");
                while (resultSet.next()) {
                    products.add(new Product(resultSet.getString(3), resultSet.getInt(1),
                            resultSet.getInt(10), resultSet.getInt(2), resultSet.getString(5),
                            resultSet.getString(4), resultSet.getDouble(8), resultSet.getDouble(6), resultSet.getDouble(7), resultSet.getBytes (9)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        /**Search View*/
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                searchfilter(newText);
                return false;
            }
        });

        /** Spinner for warehouse*/
        ArrayList<String> arrayWarehouse = new ArrayList<>();

        if (connection != null) {
            try {
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Warehouse");
                while (resultSet.next()) {
                    arrayWarehouse.add(resultSet.getString(2));

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        SWarehouse = findViewById(R.id.spinner_warehouseName);
        ArrayAdapter<String> WarehouseSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arrayWarehouse);
        SWarehouse.setPrompt("Warehouse");
        WarehouseSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SWarehouse.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        WarehouseSpinnerAdapter,
                        R.layout.contact_spinner_row_nothing_selected_warehouse,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));
        SWarehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (SWarehouse.getSelectedItem() != null) {
                    filterByWarehouse(SWarehouse.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        /** Spinner for Filters*/

        String[] arrayFilter = new String[]{
                "By Name", "By Quantity"
        };
        Spinner SFilter = (Spinner) findViewById(R.id.spinner_filterBy);
        ArrayAdapter<String> FileterSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arrayFilter);
        SFilter.setPrompt("Select Filter");
        FileterSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SFilter.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        FileterSpinnerAdapter,
                        R.layout.contact_spinner_row_nothing_selected_filter,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));
        SFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (SFilter.getSelectedItem() != null) {
                    filterbychoice(SFilter.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /** set up the RecyclerView */
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new activity_recylerviewadapter(this, products, images);
        recyclerView.setAdapter(adapter);
    }

    private void searchfilter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Product> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (Product item : products) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }

    private void filterByWarehouse(String text) {

        if (text != null) {
            ArrayList<Product> filteredlist = new ArrayList<>();

            // Mantas part of the code to check by the filter
            Connection connection = SQLConnection.CONN();
            Statement statement = null;

            if (connection != null) {
                try {
                    statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM WarehouseItem WHERE WarehouseID = (SELECT ID FROM Warehouse WHERE [Address] = '" + text + "')");
                    while (resultSet.next()) {
                        filteredlist.add(new Product(resultSet.getString(3), resultSet.getInt(1),
                                resultSet.getInt(10), resultSet.getInt(2), resultSet.getString(5),
                                resultSet.getString(4), resultSet.getDouble(8), resultSet.getDouble(6), resultSet.getDouble(7), resultSet.getBytes (9)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
            adapter.filterList(filteredlist);
        } else {
            recyclerView = findViewById(R.id.recyclerview);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new activity_recylerviewadapter(this, products, images);
            recyclerView.setAdapter(adapter);
        }
    }

    private void filterbychoice(String text) {
        ArrayList<Product> filteredlist = adapter.getProducts();
        ArrayList<Product> copy;
        switch (text) {
            case "By Name": copy = BubblesortName(filteredlist);
            adapter.filterList(copy);
            break;
            case "By Quantity": copy = BubblesortQuantity(filteredlist);
            adapter.filterList(copy);
            break;
            default:recyclerView = findViewById(R.id.recyclerview);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                adapter = new activity_recylerviewadapter(this, products, images);
                recyclerView.setAdapter(adapter);
                break;
        }
    }

    public ArrayList<Product> BubblesortName(ArrayList<Product> products) {
        ArrayList<String> names = new ArrayList<>();
        for (Product product : products) {
            names.add(product.getName());
        }
        int n = products.size();
        for (int i = 0; i < n - 1; i++)
            for (int j = 0; j < n - i - 1; j++)
                if (names.get(j).compareTo(names.get(j + 1)) > 0) {
                    // swap product array
                    Product temp = products.get(j);
                    products.set(j, products.get(j + 1));
                    products.set(j + 1, temp);

                    //Swap names array
                    String ntemp = names.get(j);
                    names.set(j, names.get(j + 1));
                    names.set(j + 1, ntemp);
                }
        return products;
    }

    public ArrayList<Product> BubblesortQuantity(ArrayList<Product> products) {
        ArrayList<Integer> num = new ArrayList<Integer>();
        for (Product product : products) {
            num.add(product.getQuantity());
        }
        int n = products.size();
        for (int i = 0; i < n - 1; i++)
            for (int j = 0; j < n - i - 1; j++)
                if (num.get(j)>(num.get(j + 1))) {
                    // swap product array
                    Product temp = products.get(j);
                    products.set(j, products.get(j + 1));
                    products.set(j + 1, temp);

                    //Swap names array
                    int ntemp = num.get(j);
                    num.set(j, num.get(j + 1));
                    num.set(j + 1, ntemp);
                }
        return products;
    }

    @Override
    protected void onStart () {
        super.onStart ();
        Connection connection = SQLConnection.CONN();
        Statement statement = null;
        String wrh = "";
        if (getIntent().hasExtra("tag") && getIntent ().getIntExtra ("tag",0)!=0) {
            warehousenumber = getIntent ().getIntExtra ("tag", 0);
            if (connection != null) {
                try {
                    statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM Warehouse WHERE ID = " + warehousenumber);
                    while (resultSet.next()) {
                        wrh = resultSet.getString ("Address");}

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
            filterByWarehouse (wrh);
        }
    }
}