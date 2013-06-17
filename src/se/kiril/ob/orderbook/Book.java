package se.kiril.ob.orderbook;

import java.util.ArrayList;

public class Book {

    @Deprecated
	private ArrayList<Order> bidsHeap = new ArrayList<Order>();
    @Deprecated
	private ArrayList<Order> asksHeap = new ArrayList<Order>();


    



	public Book(){

	}
	public void addOrder(Order pOrd){
		if (pOrd.getSide() == 'B'){
			bidsHeap.add(pOrd);
		}else if (pOrd.getSide() == 'S'){
			asksHeap.add(pOrd);
		}
	}
	public void setOrderQty(String pOrdId, int pQty){
		if (pOrdId.charAt(pOrdId.length()-1)=='B'){ // searching the bids heap
			if (pQty <= 0){ // if passed quantity <= 0 remove order
				for (int i=0; i<bidsHeap.size(); i++){ 
					if (bidsHeap.get(i).getOrdId().equals(pOrdId)){
						bidsHeap.remove(i);
					}
				}
			}else{ // else set the quantity
				for (int i=0; i<bidsHeap.size(); i++){ 
					if (bidsHeap.get(i).getOrdId().equals(pOrdId)){
						bidsHeap.get(i).setQty(pQty);
					}
				}
			}
		}else if (pOrdId.charAt(pOrdId.length()-1)=='S'){ // searching the asks heap
			if (pQty <= 0){ // if passed quantity <= 0 remove order
				for (int i=0; i<asksHeap.size(); i++){ 
					if (asksHeap.get(i).getOrdId().equals(pOrdId)){
						asksHeap.remove(i);
					}
				}
			}else{ // else set the quantity
				for (int i=0; i<asksHeap.size(); i++){
					if (asksHeap.get(i).getOrdId().equals(pOrdId)){
						asksHeap.get(i).setQty(pQty);
					}
				}
			}
		}
	}
}
