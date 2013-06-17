package se.kiril.ob.commons;

import java.io.IOException;

import se.kiril.ob.inputs.fs.ParseFile;
import se.kiril.ob.inputs.fs.ParseMessage;
import se.kiril.ob.orderbook.Book;
import se.kiril.ob.orderbook.Order;

public class ObTest {
	public static void main(String[] args) throws IOException{
		long startTime = System.nanoTime();
		/////////////
		
		Book ob = new Book();
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
