package se.kiril.ob.orderbook;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


import se.kiril.ob.orderbook.Symbol;

public class OrderBook {

    private Map<String, Symbol> symbols = new LinkedHashMap<String, Symbol>();
    private Map<String, Order> ordersMap = new HashMap<String, Order>();

    public OrderBook(){

    }
    public void addOrder(Order ord){
        if(checkSymbolExists(ord.getSymbol())){
            symbols.get(ord.getSymbol()).addOrd(ord);
            ordersMap.put(ord.getOrdId(), ord);
        }else{
            createNewSymbol(ord.getSymbol());
            symbols.get(ord.getSymbol()).addOrd(ord);
            ordersMap.put(ord.getOrdId(), ord);
        }

    }
    public void removeOrder(String ordId){
        removeOrderFromOb(ordersMap.get(ordId));
        ordersMap.remove(ordId);
    }

    private void removeOrderFromOb(Order ord){
        if (checkSymbolExists(ord.getSymbol())){
            symbols.get(ord.getSymbol()).removeOrder(ord);
            if (symbols.get(ord.getSymbol()).getTotaNolLimits() <= 0){
                symbols.remove(ord.getSymbol());
            }
        }else{
            System.err.println("Symbol doesn't exist in orderbook!");
        }
    }
    private void createNewSymbol(String pSymName){
        Symbol newSymbol = new Symbol(pSymName);
        symbols.put(newSymbol.getSymbolName(), newSymbol);
    }
    private boolean checkSymbolExists(String pSymName){
        if (symbols.containsKey(pSymName)){
            return true;
        }else{
            return false;
        }
    }
}
