package se.kiril.ob.orderbook;

/**
 * Created with IntelliJ IDEA.
 * User: kiril
 * Date: 6/20/13
 * Time: 12:00 AM
 * To change this template use File | Settings | File Templates.
 */
import java.util.ArrayList;

import se.kiril.ob.orderbook.commons.LimitsBinTree;

public class Symbol {

    private String symbolName;

    private LimitsBinTree bids = new LimitsBinTree();
    private LimitsBinTree asks = new LimitsBinTree();

    public Symbol(String pSymName){
        setSymbolName(pSymName);
    }

    public void addOrd(Order pOrd){
        insertOrderToTree(pOrd);
    }


    private void createNewLimit(char pSide, double pPrice, int pVol, Order pOrd){
        Limit lim = new Limit(pPrice);
        lim.addToSize(pVol);
        lim.addOrderToLimit(pOrd);

        if (pSide == 'B'){
            bids.addLimit(lim);

        }else{
            asks.addLimit(lim);
        }
    }
    private void insertOrderToTree(Order ord){
        if (checkLimitExists(ord.getSide(), ord.getLimit())){
            //add order to an existing limit
            //!!!!
        }else{
            createNewLimit(ord.getSide(), ord.getLimit(), ord.getQty(), ord);
        }
    }

    private boolean checkLimitExists(char pSide, double pLimit){
        if (pSide == 'B'){ // check in bids tree
            if (bids.search(pLimit) != null){
                return true;
            }else{
                return false;
            }
        }else{ // check in asks tree
            if (asks.search(pLimit) != null){
                return true;
            }else{
                return false;
            }
        }
    }

//	public void setOrderQty(String pOrdId, int pQty){
//		if (pOrdId.charAt(pOrdId.length()-1)=='B'){ // searching the bids heap
//			if (pQty <= 0){ // if passed quantity <= 0 remove order
//				for (int i=0; i<bidsHeap.size(); i++){
//					if (bidsHeap.get(i).getOrdId().equals(pOrdId)){
//						bidsHeap.remove(i);
//					}
//				}
//			}else{ // else set the quantity
//				for (int i=0; i<bidsHeap.size(); i++){
//					if (bidsHeap.get(i).getOrdId().equals(pOrdId)){
//						bidsHeap.get(i).setQty(pQty);
//					}
//				}
//			}
//		}else if (pOrdId.charAt(pOrdId.length()-1)=='S'){ // searching the asks heap
//			if (pQty <= 0){ // if passed quantity <= 0 remove order
//				for (int i=0; i<asksHeap.size(); i++){
//					if (asksHeap.get(i).getOrdId().equals(pOrdId)){
//						asksHeap.remove(i);
//					}
//				}
//			}else{ // else set the quantity
//				for (int i=0; i<asksHeap.size(); i++){
//					if (asksHeap.get(i).getOrdId().equals(pOrdId)){
//						asksHeap.get(i).setQty(pQty);
//					}
//				}
//			}
//		}
//	}

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }


}

