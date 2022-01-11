package com.example.product_inventory_application;

public class Product {

    private String name;
    private int id;
    private int quantitiy;
    private int warehouseid;
    private String barcodeid;
    private String description;
    private double weight;
    private double heigth;
    private double length;

    public Product(String name, int id, int quantitiy, int warehouseid, String barcodeid, String description, double weight, double heigth, double length) {
        this.name = name;
        this.id = id;
        this.quantitiy = quantitiy;
        this.warehouseid = warehouseid;
        this.barcodeid = barcodeid;
        this.description = description;
        this.weight = weight;
        this.heigth = heigth;
        this.length = length;
    }

    public String getBarcodeid() {
        return barcodeid;
    }

    public void setBarcodeid(String barcodeid) {
        this.barcodeid = barcodeid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantitiy() {
        return quantitiy;
    }

    public void setQuantitiy(int quantitiy) {
        this.quantitiy = quantitiy;
    }

    public int getWarehouseid() {
        return warehouseid;
    }

    public void setWarehouseid(int warehouseid) {
        this.warehouseid = warehouseid;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeigth() {
        return heigth;
    }

    public void setHeigth(double heigth) {
        this.heigth = heigth;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
}
