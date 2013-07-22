package se.kiril.tstest.om.commons;

import java.awt.List;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import se.kiril.tstest.om.input.ParseFile;
import se.kiril.tstest.om.orderbook.OrderBook;
import se.kiril.tstest.om.reports.ExecutionReport;

public class ObTest {
	public static void main(String[] args) throws IOException {
		List fLines = new List();

		OrderBook ob = new OrderBook();
		ParseFile pf = new ParseFile("trades.in");
		fLines = pf.getParsedFile();
		// PricesGui gui = new PricesGui();

		// long startTime = System.nanoTime();
		// for (int i = 0; i < fLines.getItemCount(); i++) {
		// ParseMessage pm = new ParseMessage(fLines.getItem(i));
		// Order newOrd = new Order(pm.getSymbol(), pm.getSide(),
		// pm.getOrdType(), pm.getPrice(), pm.getQty(), pm.getUser());
		// printExecReports(ob.addOrder(newOrd));
		// // ob.addOrder(newOrd);
		// ob.execOrder(newOrd);
		// printExecReports(ob.execOrder(newOrd));
		// // gui.setTxt(ob.getPrices());
		// // printCurrentPrices(ob.getPrices());
		// }
		//
		// long estTime = System.nanoTime() - startTime;
		// System.out.println("Execution time: " + (double) estTime / 1000000
		// + " ms");
		//
	}

	public static void printExecReports(ExecutionReport report) {
		System.out.println(report.getExecId());
	}

	public static void printCurrentPrices(HashMap prices) {
		HashMap<String, Double[]> tPr = new HashMap<String, Double[]>();
		tPr = prices;
		for (Map.Entry<String, Double[]> e : tPr.entrySet()) {
			System.out.format("%9s%9s%9s%9s%9s", e.getKey(), "Bid:",
					e.getValue()[0], "Ask:", e.getValue()[1]);
			System.out.println("\n");
		}
		System.out.println("=================================================");
	}

}
