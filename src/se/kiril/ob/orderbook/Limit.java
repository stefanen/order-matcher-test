package se.kiril.ob.orderbook;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: kiril
 * Date: 6/16/13
 * Time: 8:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class Limit {
    private double price;
    private int size;
    private int totalVolume;

    private LinkedList<Order> orders = new LinkedList<Order>();

    public Limit(int price){
        this.price = price;
    }
    public void addOrderToLimit(Order ord){
        if (validateOrder(ord)){

        }else{
            System.err.println("Wrong order (price)");
        }

    }
    private boolean validateOrder(Order ord){
        if (ord.getLimit() == price){
            return true;
        }else{
            return false;
        }
    }
}
