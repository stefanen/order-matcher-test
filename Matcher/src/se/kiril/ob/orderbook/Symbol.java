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
        //TODO This is not very efficient, need to change it.
        purgeEmptyLimits();
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
                bidLimits.remove(ord.getLimit());
            }
        }else{ // asks side
            askLimits.get(ord.getLimit()).removeOrderFromLimit(ord);
            if(askLimits.get(ord.getLimit()).getSize() <= 0){
                askLimits.remove(ord.getLimit());
            }
        }
    }

    private void addOrdToLimit(Order ord){
        if (ord.getSide()=='B'){ // bids
            if(bidLimits.containsKey(ord.getLimit())){ // limit already exists
                bidLimits.get(ord.getLimit()).addOrderToLimit(ord);
                // EXECUTE ORDER HERE
                executeOrder(ord);
            }else{
                createLimitAndAddOrd(ord);
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
    public void purgeEmptyLimits(){
        clearEmptyAskLimits();
        clearEmptyBidLimits();
    }
    private void clearEmptyAskLimits(){
        if (!askLimits.isEmpty()){
            Map<Double, Limit> tempMap = new TreeMap<Double, Limit>();
            for (Map.Entry<Double, Limit> e : askLimits.entrySet()){
                tempMap.put(e.getKey(), e.getValue());
                if (e.getValue().getSize() <= 0){
                    tempMap.remove(e.getKey());
                }
            }
            askLimits = tempMap;
        }
    }
    private void clearEmptyBidLimits(){
        if (!bidLimits.isEmpty()){
            Map<Double, Limit> tempMap = new TreeMap<Double, Limit>();
            for (Map.Entry<Double, Limit> e : bidLimits.entrySet()){
                tempMap.put(e.getKey(), e.getValue());
                if (e.getValue().getSize() <= 0){
                    tempMap.remove(e.getKey());
                }
            }
            bidLimits = tempMap;
        }
    }
    private void executeOrder(Order ord){
        if (ord.getSide() == 'B'){
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
            //clearEmptyAskLimits(); //not very efficient
            ord.setQty(ord.getQty()-tradedQty);
            bidLimits.get(ord.getLimit()).removeFromSize(tradedQty);
        }else{
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
            //clearEmptyBidLimits(); /not very efficient
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
