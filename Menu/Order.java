package com.example.menu;

import java.text.DecimalFormat;
import java.util.ArrayList;
/*
the order class represents an array of items and is built as such -> order = {item1Name,item1Price,item1Quantity,item2Name,item2Price,etc...}
 */
public class Order {
    private ArrayList<String> order = new ArrayList<String>();
    public void add(Item item){
        order.add(item.getItemName());
        order.add(item.getItemPrice());
        order.add(item.getItemQuantity());
    }
    public void remove(Item item){
        int index = order.indexOf(item.getItemName());
        order.remove(index);
        order.remove(index);
        order.remove(index);
    }
    public String getOrder(String totalCost) {
        // remove extra decimal numbers
        DecimalFormat df = new DecimalFormat("#.##");
        String newOrder = "Your order is: \n";
        for (int i = 0; i < order.size() - 2; i += 3) {
            newOrder += order.get(i + 2) + "x " + order.get(i);
            newOrder += ", ";
            try {
                newOrder += df.format(Double.parseDouble(order.get(i + 1)) * Double.parseDouble(order.get(i + 2))) + "₪\n";
            } catch (Exception e) {
                System.out.println("Error! method tried to parse a String- Please re-enter menu in correct format");
            }

        }
        newOrder += "--------------------------------------------------------------------------------------";
        newOrder += "\nTotal = "+totalCost+"₪";
        return newOrder;
    }

}
