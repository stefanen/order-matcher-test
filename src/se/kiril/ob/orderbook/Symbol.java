package se.kiril.ob.orderbook;


import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;


public class Symbol {

    private String symbolName;

    private Map<Double, Limit> bidLimits = new TreeMap<Double, Limit>(Collections.reverseOrder());
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
            int tradedQty = 0;
            int remainingVol = ord.getQty();
            for (Map.Entry<Double, Limit> askLimit : askLimits.entrySet()){
                if (remainingVol > 0 && askLimit.getValue().getPrice() < ord.getLimit()){
                    int tVol = 0;
                    tVol = askLimit.getValue().popFromInsideOfLimit(remainingVol);
                    tradedQty += tVol;
                    remainingVol -= tVol;
                }else{
                    break;
                }
            }
            ord.setQty(ord.getQty()-tradedQty);
            bidLimits.get(ord.getLimit()).removeFromSize(tradedQty);
        }else{ // if order is ask, look for a match in bids. Looking for a bigger or equal bid value.
            // the bidLimit tree map is already reverse sorted
            int tradedQty = 0;
            int remainingVol = ord.getQty();
            for (Map.Entry<Double, Limit> bidLimit : bidLimits.entrySet()){
                if (remainingVol > 0 && bidLimit.getValue().getPrice() > ord.getLimit()){
                    int tVol = 0;
                    tVol = bidLimit.getValue().popFromInsideOfLimit(remainingVol);
                    tradedQty += tVol;
                    remainingVol -= tVol;
                }else{
                    break;
                }
            }
            ord.setQty(ord.getQty()-tradedQty);
            askLimits.get(ord.getLimit()).removeFromSize(tradedQty);
        }
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }


}