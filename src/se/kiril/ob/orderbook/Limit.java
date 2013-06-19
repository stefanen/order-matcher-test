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

    public Limit(double pPrice){
        this.price = pPrice;
    }
    public void addOrderToLimit(Order pOrder){
        orders.add(pOrder);
    }

    public double getPrice(){
        return price;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public void addToSize(int pVol){
        this.size += pVol;
    }
}
