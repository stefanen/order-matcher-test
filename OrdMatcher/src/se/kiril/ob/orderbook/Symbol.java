package se.kiril.ob.orderbook;


import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import se.kiril.ob.orderbook.commons.LimitsBinTree;


public class Symbol {

    private String symbolName;

    private Map<Double, Limit> limitBids = new TreeMap<Double, Limit>();
    private Map<Double, Limit> limitAsks = new TreeMap<Double, Limit>();
    
    private LinkedList<Order> marketBids = new LinkedList<Order>();
    private LinkedList<Order> marketAsks = new LinkedList<Order>();
    
    protected double bestBid = 0.0;
    protected double bestAsk = 0.0;

    public Symbol(String pSymName){
        setSymbolName(pSymName);

    }
    //TODO create execution reports

    public void addOrd(Order pOrd){
        addOrdToLimit(pOrd);
        //TODO This is not very efficient, need to change it.
        purgeEmptyLimits();
    }
    public void removeOrder(Order ord){
        removeOrdFromLimit(ord);
    }
    public int getTotaNolLimits(){
        return limitBids.size() + limitAsks.size();
    }

    private void createLimitAndAddOrd(Order ord){
        Limit lim = new Limit(ord.getLimit());
        lim.addOrderToLimit(ord);
        if (ord.getSide() == 'B'){
            limitBids.put(lim.getPrice(), lim);

        }else{
            limitAsks.put(lim.getPrice(), lim);
        }
    }
    private void removeOrdFromLimit(Order ord){
        if (ord.getSide()== 'B'){ // bids side
            limitBids.get(ord.getLimit()).removeOrderFromLimit(ord);
            if(limitBids.get(ord.getLimit()).getSize() <= 0){
                limitBids.remove(ord.getLimit());
            }
            bestBid = getHighestBid(ord);
        }else{ // asks side
            limitAsks.get(ord.getLimit()).removeOrderFromLimit(ord);
            if(limitAsks.get(ord.getLimit()).getSize() <= 0){
                limitAsks.remove(ord.getLimit());
            }
            bestAsk = getLowestAsk(ord);
        }
    }

    private void addOrdToLimit(Order ord){
    	//=====MARKET ORDER=====
    	if(ord.getOrdType()=='M'){
    		if (ord.getSide()=='B'){ //bids
    			marketBids.add(ord);
    			//EXECUTE
    			executeOrder(ord);
    		}else{ //asks
    			marketAsks.add(ord);
    			//EXECUTE
    			executeOrder(ord);
    		}
    	//=====LIMIT ORDER=====
    	}else if (ord.getOrdType()=='L'){
    		if (ord.getSide()=='B'){ // bids
                if(limitBids.containsKey(ord.getLimit())){ // limit already exists
                    limitBids.get(ord.getLimit()).addOrderToLimit(ord);
                    // EXECUTE ORDER HERE
                    executeOrder(ord);
                }else{
                    createLimitAndAddOrd(ord);
                    // EXECUTE ORDER HERE
                    executeOrder(ord);
                }
                bestBid = getHighestBid(ord);
            }else{ // asks
                if (limitAsks.containsKey(ord.getLimit())){
                    limitAsks.get(ord.getLimit()).addOrderToLimit(ord);
                    // EXECUTE ORDER HERE
                    executeOrder(ord);
                }else{
                    createLimitAndAddOrd(ord);
                    // EXECUTE ORDER HERE
                    executeOrder(ord);
                }
                bestAsk = getLowestAsk(ord);
            }
    	//TODO
    	//=====PEG ORDER=====
    	}else if (ord.getOrdType()=='P'){
    		
    	}
    }
    public void purgeEmptyLimits(){
    	//TODO This could be multithreaded
        clearEmptyLimitAsks();
        clearEmptyLimitBids();
        clearEmptyMarketAsks();
        clearEmptyMarketBids();
    }
    private void clearEmptyMarketAsks(){
    	if (!marketAsks.isEmpty()){
    		for (Iterator<Order> it = marketAsks.iterator(); it.hasNext();){
                if(it.next().getQty() <= 0){
                    it.remove();
                }
            }
    	}
    }
    private void clearEmptyMarketBids(){
    	if (!marketBids.isEmpty()){
    		for (Iterator<Order> it = marketBids.iterator(); it.hasNext();){
                if(it.next().getQty() <= 0){
                    it.remove();
                }
            }
    	}
    }
    private void clearEmptyLimitAsks(){
        if (!limitAsks.isEmpty()){
            Map<Double, Limit> tempMap = new TreeMap<Double, Limit>();
            for (Map.Entry<Double, Limit> e : limitAsks.entrySet()){
                tempMap.put(e.getKey(), e.getValue());
                if (e.getValue().getSize() <= 0){
                    tempMap.remove(e.getKey());
                }
            }
            limitAsks = tempMap;
        }
    }
    private void clearEmptyLimitBids(){
        if (!limitBids.isEmpty()){
            Map<Double, Limit> tempMap = new TreeMap<Double, Limit>();
            for (Map.Entry<Double, Limit> e : limitBids.entrySet()){
                tempMap.put(e.getKey(), e.getValue());
                if (e.getValue().getSize() <= 0){
                    tempMap.remove(e.getKey());
                }
            }
            limitBids = tempMap;
        }
    }
    private void executeOrder(Order ord){
    	if (ord.getOrdType()=='L'){
    		executeLimitOrder(ord);
    	}else if(ord.getOrdType()=='M'){
    		executeMarketOrder(ord);
    	}
    }
    
    
    private void executeMarketOrder(Order ord){
    	if (ord.getSide()=='B'){ // bid
    		int tradedQty = 0;
    		int remainingVol = ord.getQty();
    		for (Map.Entry<Double, Limit> askLimit : limitAsks.entrySet()){
                if (remainingVol > 0){
                    int tVol = 0;
                    tVol = askLimit.getValue().popFromInsideOfLimit(remainingVol);
                    tradedQty += tVol;
                    remainingVol -= tVol;
                }else{
                    break;
                }
            }
    		ord.setQty(ord.getQty()-tradedQty);
            //bidLimits.get(ord.getLimit()).removeFromSize(tradedQty);
    	}else{ // ask 
    		int tradedQty = 0;
            int remainingVol = ord.getQty();
            List<Double> listBidLimits = new LinkedList<Double>();
            for (Map.Entry<Double, Limit> bLimit : limitBids.entrySet()){
            	listBidLimits.add(bLimit.getKey());
            }           
            for (int i=listBidLimits.size()-1; i>=0; i--){  	
            	//listBidLimits.get(i);
            	if (remainingVol > 0){
                    int tVol = 0;
                    tVol = limitBids.get(listBidLimits.get(i)).popFromInsideOfLimit(remainingVol);
                    tradedQty += tVol;
                    remainingVol -= tVol;
                }else{
                    break;
                }	
            }
            ord.setQty(ord.getQty()-tradedQty);
            //askLimits.get(ord.getLimit()).removeFromSize(tradedQty);
    	}
    }
    
    private Double getHighestBid(Order ord){
    	Double highestBid = ord.getLimit();
    	for (Map.Entry<Double, Limit> bidLimit : limitBids.entrySet()){
    		if(highestBid!=null && highestBid < bidLimit.getKey()){
    			highestBid = bidLimit.getKey();
    		}
    	}
    	if (highestBid!= null){
    		return highestBid;
    	}else{
    		return 0.0;
    	}
    }
    private Double getLowestAsk(Order ord){
    	Double lowestAsk = ord.getLimit();
    	for (Map.Entry<Double, Limit> askLimit : limitAsks.entrySet()){
    		if(lowestAsk!=null && lowestAsk > askLimit.getKey()){
    			lowestAsk = askLimit.getKey();
    		}
    	}
    	if (lowestAsk!= null){
    		return lowestAsk;
    	}else{
    		return 0.0;
    	}
    	
    }
    
    private void executeLimitOrder(Order ord){
        if (ord.getSide() == 'B'){
            int tradedQty = 0;
            int remainingVol = ord.getQty();
            //Going through market orders first
            for (Order order : marketAsks){
                if (remainingVol > 0){
                    int tVol = 0;
                    tVol = order.trade(remainingVol);
                    tradedQty += tVol;
                    remainingVol -= tVol;
                }else{
                    break;
                }
            }
            //Going through limits
            for (Map.Entry<Double, Limit> askLimit : limitAsks.entrySet()){
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
            limitBids.get(ord.getLimit()).removeFromSize(tradedQty);
        }else{
            int tradedQty = 0;
            int remainingVol = ord.getQty();
          //Going through market orders first
            //TODO not correct ??
            for (Order order : marketBids){
                if (remainingVol > 0){
                    int tVol = 0;
                    tVol = order.trade(remainingVol);
                    tradedQty += tVol;
                    remainingVol -= tVol;
                }else{
                    break;
                }
            }
          //Going through limits
            List<Double> listBidLimits = new LinkedList<Double>();
            for (Map.Entry<Double, Limit> bLimit : limitBids.entrySet()){
            	listBidLimits.add(bLimit.getKey());
            }
            for (int i=listBidLimits.size()-1; i>=0; i--){  	
            	//listBidLimits.get(i);
            	if (remainingVol > 0 && limitBids.get(listBidLimits.get(i)).getPrice() > ord.getLimit()){
                    int tVol = 0;
                    tVol = limitBids.get(listBidLimits.get(i)).popFromInsideOfLimit(remainingVol);
                    tradedQty += tVol;
                    remainingVol -= tVol;
                }else{
                    break;
                }	
            }
            //clearEmptyBidLimits(); /not very efficient
            ord.setQty(ord.getQty()-tradedQty);
            limitAsks.get(ord.getLimit()).removeFromSize(tradedQty);
        }
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }
    public Double[] getBestBidBestAsk(){
    	Double[] prices = new Double[2];
    	prices[0] = bestBid;
    	prices[1] = bestAsk;
    	return prices;
    }

}
