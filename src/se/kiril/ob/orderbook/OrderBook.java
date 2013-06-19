package se.kiril.ob.orderbook;

import java.util.ArrayList;
import java.util.HashMap;

import se.kiril.ob.orderbook.Symbol;

public class OrderBook {

    //This should be a set!
    private HashMap<String, Symbol> symbolsHeap = new HashMap<String, Symbol>();
    public OrderBook(){

    }
    public void addOrder(Order ord){
        if(checkSymbolExists(ord.getSymbol())){
            symbolsHeap.get(ord.getSymbol()).addOrd(ord);
        }else{
            createNewSymbol(ord.getSymbol());
            symbolsHeap.get(ord.getSymbol()).addOrd(ord);
        }
    }

    private void createNewSymbol(String pSymName){
        Symbol newSymbol = new Symbol(pSymName);
        symbolsHeap.put(newSymbol.getSymbolName(), newSymbol);
    }
    private boolean checkSymbolExists(String pSymName){
        if (symbolsHeap.containsKey(pSymName)){
            return true;
        }else{
            return false;
        }
    }
}
