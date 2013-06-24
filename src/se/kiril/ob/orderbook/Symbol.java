package se.kiril.ob.orderbook;


import java.util.Map;
import java.util.TreeMap;


public class Symbol {

    private String symbolName;

    private Map<Double, Limit> bidLimits = new TreeMap<Double, Limit>();
    private Map<Double, Limit> askLimits = new TreeMap<Double, Limit>();

    public Symbol(String pSymName){
        setSymbolName(pSymName);
    }

    public void addOrd(Order pOrd){
        addOrdToLimit(pOrd);
    }
    public void removeOrder(Order ord){
        removeOrdFromLimit(ord);
    }
    public int getTotaNolLimits(){
        return bidLimits.size() + askLimits.size();
    }

    private void createLimitAndAddOrd(Order ord){
        Limit lim = new Limit(ord.getLimit());
        lim.addOrderToLimit(ord);
        if (ord.getSide() == 'B'){
            bidLimits.put(lim.getPrice(), lim);

        }else{
            askLimits.put(lim.getPrice(), lim);
        }
    }
    private void removeOrdFromLimit(Order ord){
        if (ord.getSide()== 'B'){ // bids side
            bidLimits.get(ord.getLimit()).removeOrderFromLimit(ord);
            if(bidLimits.get(ord.getLimit()).getSize() <= 0){
                bidLimits.remove(ord.getLimit()); // if the limit qty is <= 0, limit is removed
            }
        }else{ // asks side
            askLimits.get(ord.getLimit()).removeOrderFromLimit(ord);
            if(askLimits.get(ord.getLimit()).getSize() <= 0){
                askLimits.remove(ord.getLimit()); // if the limit qty is <= 0, limit is removed
            }
        }
    }

    private void addOrdToLimit(Order ord){
        if (ord.getSide()=='B'){ // bids
            if(bidLimits.containsKey(ord.getLimit())){ // limit already exists
                bidLimits.get(ord.getLimit()).addOrderToLimit(ord);
                // EXECUTE ORDER HERE
                executeOrder(ord);
            }else{ // limit doesnt exist yet
                createLimitAndAddOrd(ord); // creating limit and adding an order
                // EXECUTE ORDER HERE
                executeOrder(ord);
            }
        }else{ // asks
            if (askLimits.containsKey(ord.getLimit())){
                askLimits.get(ord.getLimit()).addOrderToLimit(ord);
                // EXECUTE ORDER HERE
                executeOrder(ord);
            }else{
                createLimitAndAddOrd(ord);
                // EXECUTE ORDER HERE
                executeOrder(ord);
            }
        }
    }
    private void executeOrder(Order ord){
        if (ord.getSide() == 'B'){ // if order is bid, look for a match in asks. Looking for a smaller or equal ask value

            for (Map.Entry<Double, Limit> askLimit : askLimits.entrySet()){

                if(ord.getLimit() >= askLimit.getKey()){
                    askLimit.getValue().popFromInsideOfBook(35);
//					if(askLimit.getValue().getSize() >= ord.getQty()){
//						System.out.println("all in this limit");
//						break;
//					}
//					else{
//						System.out.println("moving on to next limit");
//					}
                }
            }


//			for (Map.Entry<Double, Limit> askLimitEntry : askLimits.entrySet()){
//				if (askLimitEntry.getKey() <= ord.getLimit()){
//
//					if (askLimitEntry.getValue().getOldestOrder().getQty() >= ord.getQty()){
//						askLimitEntry.getValue().reduceOrder(askLimitEntry.getValue().getOldestOrder(), ord.getQty());
//						removeOrdFromLimit(ord);
//						//break;
//					}else{
//						int t = ord.getQty();
//						ord.reduceQty(askLimitEntry.getValue().getOldestOrder().getQty());
//						askLimitEntry.getValue().reduceOrder(askLimitEntry.getValue().getOldestOrder(), t);
//					}
//				}
//			}
        }else{ // if order is ask, look for a match in bids. Looking for a bigger or equal bid value

        }
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }


}