package com.example.product_inventory_application;

public class Product {

    private String name;
    private int id;
    private int quantity;
    private int warehouseid;
    private String barcodeid;
    private String description;
    private double width;
    private double heigth;
    private double length;
    private byte[] image;

    public Product(String name, int id, int quantitiy, int warehouseid, String barcodeid, String description, double width, double heigth, double length, byte[] image) {
        this.name = name;
        this.id = id;
        this.quantity = quantitiy;
        this.warehouseid = warehouseid;
        this.barcodeid = barcodeid;
        this.description = description;
        this.width = width;
        this.heigth = heigth;
        this.length = length;
        this.image = image;
    }

    public byte[] getImage () { return image; }

    public void setImage (byte[] image) { this.image = image; }

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

    public int getQuantity () {
        return quantity;
    }

    public void setQuantity (int quantity) {
        this.quantity = quantity;
    }

    public int getWarehouseid() {
        return warehouseid;
    }

    public void setWarehouseid(int warehouseid) {
        this.warehouseid = warehouseid;
    }

    public double getWidth () {
        return width;
    }

    public void setWidth (double width) {
        this.width = width;
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
