package com.example.product_inventory_application;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Warehouse {
    public int id;
    public LatLng latLng;
    public String Name;
    public int capacity;
    public ArrayList<LatLng> Area;
    public ArrayList<LatLng> slots;

    public Warehouse(int Id,String Name, LatLng l, ArrayList<LatLng>area, int capacity, ArrayList<LatLng> slots )
    {
        this.id = Id;
        this.latLng = l;
        this.Name = Name;
        this.capacity = capacity;
        this.Area=area;
        this.slots=slots;

    }

    public ArrayList<LatLng> getArea () {
        return Area;
    }

    public ArrayList<LatLng> getSlots () {
        return slots;
    }

    public int getId() {
        return id;
    }


    public int getCapacity () {
        return capacity;
    }

    public String getName () {
        return Name;
    }

    public LatLng getLatLng() {
        return latLng;
    }
}
