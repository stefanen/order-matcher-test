package se.kiril.ob.commons;

import java.awt.List;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import se.kiril.ob.guis.PricesGui;
import se.kiril.ob.inputs.fs.ParseFile;
import se.kiril.ob.inputs.fs.ParseMessage;
import se.kiril.ob.orderbook.OrderBook;
import se.kiril.ob.orderbook.Order;

public class ObTest {
    public static void main(String[] args) throws IOException{
        List fLines = new List();

        OrderBook ob = new OrderBook();
        ParseFile pf = new ParseFile("trades.in");
        fLines = pf.getParsedFile();
        PricesGui gui = new PricesGui();
        
        long startTime = System.nanoTime(); 
        for (int i=0; i<fLines.getItemCount(); i++){
            ParseMessage pm = new ParseMessage(fLines.getItem(i));
            Order newOrd = new Order(pm.getSymbol(), pm.getSide(), pm.getOrdType(),
                    pm.getPrice(), pm.getQty(), pm.getUser());
            ob.addOrder(newOrd);
            gui.setTxt(ob.getPrices());
//            printCurrentPrices(ob.getPrices());
        }
        
        long estTime = System.nanoTime() - startTime;
        System.out.println("Execution time: "+(double) estTime/1000000 +" ms");

    } 
    
    public static void printCurrentPrices(HashMap prices){
    	HashMap<String, Double[]> tPr = new HashMap<String, Double[]>();
    	tPr = prices;
        for (Map.Entry<String, Double[]> e : tPr.entrySet()){
        	System.out.format("%9s%9s%9s%9s%9s",e.getKey(), "Bid:", e.getValue()[0], "Ask:", e.getValue()[1]);
        	System.out.println("\n");
        }
        System.out.println("=================================================");
    }
    
   
}
