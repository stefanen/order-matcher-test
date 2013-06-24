package se.kiril.ob.orderbook;


import java.util.Iterator;
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
    private int totalQty;
    //private int totalVolume;

    private LinkedList<Order> orders = new LinkedList<Order>();


    public Limit(double pPrice){
        this.price = pPrice;
    }
    //TODO
    public int popFromInsideOfBook(int vol){
        int tradedQty = 0;
        int remainingVol = vol;
//    	for (Order order : orders){
//    		if (remainingVol > 0){
//    			int t = order.getQty();
//        		order.reduceQty(remainingVol);
//        		if(order.getQty() <= 0){
//        			orders.remove(order);
//        			
//        			tradedQty += t;
//        			remainingVol -= tradedQty;
//        		}else{
//        			tradedQty += t-order.getQty();
//        			remainingVol -= tradedQty;
//        		}
//    		}else{
//    			break;
//    		}
//    	}
        return tradedQty;
    }


    public void addOrderToLimit(Order pOrder){
        orders.add(pOrder);
        addToSize(pOrder.getQty());
    }
    public void removeOrderFromLimit(Order pOrder){
        orders.remove(pOrder);
        removeFromSize(pOrder.getQty());
    }

    private void reduceOrder(Order pOrder, int pVol){
        if (orders.contains(pOrder)){
            pOrder.reduceQty(pVol);
            if (pOrder.getQty() <= 0){
                removeOrderFromLimit(pOrder);
            }
        }
    }

    public double getPrice(){
        return price;
    }
    public int getSize() {
        return totalQty;
    }
    private void setSize(int size) {
        this.totalQty = size;
    }
    private void addToSize(int pVol){
        this.totalQty += pVol;
    }
    private void removeFromSize(int pVol){
        this.totalQty -= pVol;
    }
}