package com.example.menu;


/*
this class represents an item
 */
public class Item {
    private String itemName;
    private String itemPrice;
    private String itemQuantity;
    public Item(String itemName, String itemPrice, String itemQuantity) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
    }
    public String getItemName() {
        return itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }
}
