package se.kiril.matcher.orderbook;

import java.io.IOException;

import se.kiril.matcher.commons.Order;
import se.kiril.matcher.commons.Orderbook;
import se.kiril.matcher.parser.ParseFile;
import se.kiril.matcher.parser.ParseMessage;

public class MatcherTest {
	public static void main(String[] args) throws IOException{
		long startTime = System.nanoTime();
		/////////////
		
		Orderbook ob = new Orderbook();
		ParseFile pf = new ParseFile("refInput.in");
		
		for (int i=0; i<pf.getNumberOfLines(); i++){
			ParseMessage pm = new ParseMessage(pf.getLine(i));
			Order newOrd = new Order(pm.getSymbol(), pm.getSide(), 
					pm.getPrice(), pm.getQty(), pm.getUser());
			ob.addOrder(newOrd);	
		}
		
		////////////
		long estTime = System.nanoTime() - startTime;
		System.out.println("Execution time: "+(double) estTime/1000000 +" ms");
	}
}
