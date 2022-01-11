package com.example.product_inventory_application;

import android.app.Application;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class activity_history extends AppCompatActivity {
    private RecyclerView recyclerView;
    private activity_recyclerviewadapterhistory adapter;
    private ArrayList<Event> events = new ArrayList<>();
    private ImageView back;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        back = findViewById(R.id.btn_historyBack);
        back.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                startActivity (new Intent(activity_history.this,activity_home.class));
            }
        });

        getevents(events);
        recyclerView = findViewById(R.id.recyclerViewForHistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager (this));
        adapter = new activity_recyclerviewadapterhistory (this, events);
        recyclerView.setAdapter(adapter);
        // Spinner for filters
        String[] arrayFilters = new String[]{
                "By Name", /*"By date", "By time",*/ "By Action"
        };
        Spinner SFilter = (Spinner) findViewById(R.id.spinnerForHistoryFilters);
        ArrayAdapter<String> FilterSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arrayFilters);
        SFilter.setPrompt("Select Filter");
        FilterSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SFilter.setAdapter(FilterSpinnerAdapter);

        SFilter.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
                if (SFilter.getSelectedItem() != null) {
                    filterhistory (SFilter.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected (AdapterView<?> parent) { }
        });
    }

    private void filterhistory(String text) {
        ArrayList<Event> filteredlist = adapter.getEvents ();
        ArrayList<Event> copy;
        switch (text) {
            case "By Name": copy = BubblesortName(filteredlist);
                adapter.filterList(copy);
                break;
            case "By Action": copy = BubblesortAction (filteredlist);
                adapter.filterList(copy);
                break;
            default:
                recyclerView = findViewById(R.id.recyclerViewForHistory);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                adapter = new activity_recyclerviewadapterhistory (this, events);
                recyclerView.setAdapter(adapter);
                break;
        }
    }

    private void getevents (ArrayList<Event> events) {

        Connection connection = SQLConnection.CONN();
        Statement statement = null;

        if (connection != null) {
            try {
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM ChangeLog");
                while (resultSet.next()) {
                    events.add(new Event (resultSet.getString(4), resultSet.getString(5),
                            resultSet.getString (6), resultSet.getString(7)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public ArrayList<Event> BubblesortName(ArrayList<Event> events) {
        ArrayList<String> names = new ArrayList<>();
        for (Event event : events) {
            names.add(event.getName());
        }
        int n = events.size();
        for (int i = 0; i < n - 1; i++)
            for (int j = 0; j < n - i - 1; j++)
                if (names.get(j).compareTo(names.get(j + 1)) > 0) {
                    // swap product array
                    Event temp = events.get(j);
                    events.set(j, events.get(j + 1));
                    events.set(j + 1, temp);

                    //Swap names array
                    String ntemp = names.get(j);
                    names.set(j, names.get(j + 1));
                    names.set(j + 1, ntemp);
                }
        return events;
    }

    public ArrayList<Event> BubblesortAction(ArrayList<Event> events) {
        ArrayList<String> actions = new ArrayList<>();
        for (Event event : events) {
            actions.add(event.getAction ());
        }
        int n = events.size();
        for (int i = 0; i < n - 1; i++)
            for (int j = 0; j < n - i - 1; j++)
                if (actions.get(j).compareTo(actions.get(j + 1)) > 0) {
                    // swap product array
                    Event temp = events.get(j);
                    events.set(j, events.get(j + 1));
                    events.set(j + 1, temp);

                    //Swap names array
                    String ntemp = actions.get(j);
                    actions.set(j, actions.get(j + 1));
                    actions.set(j + 1, ntemp);
                }
        return events;
    }
}
