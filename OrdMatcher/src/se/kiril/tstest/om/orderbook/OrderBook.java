package se.kiril.tstest.om.orderbook;

import java.util.LinkedHashMap;
import java.util.Map;

import se.kiril.tstest.om.reports.ExecutionReport;
import se.kiril.tstest.om.symbols.Symbol;

public class OrderBook {

	private Map<String, Symbol> symbols = new LinkedHashMap<String, Symbol>();

	// private Map<String, Order> ordersMap = new HashMap<String, Order>();

	public OrderBook() {

	}
	//DebugOnly
	public Map<String, Symbol> getSymbolsMap(){
		return symbols;
	}
	
	public void execOrder(Order ord) {
		if (checkSymbolExists(ord.getSymbol())) {
			symbols.get(ord.getSymbol()).execOrd(ord);
		} else {
		}
	}

	public ExecutionReport addOrder(Order ord) {
		if (checkSymbolExists(ord.getSymbol())) {
			return symbols.get(ord.getSymbol()).addOrd(ord);
		} else {
			createNewSymbol(ord.getSymbol());
			return symbols.get(ord.getSymbol()).addOrd(ord);
		}
	}

	public void removeOrder(String ordId) {
		// removeOrderFromOb(ordersMap.get(ordId));
		// ordersMap.remove(ordId);
	}

	@SuppressWarnings("unused")
	private void removeOrderFromOb(Order ord) {
		if (checkSymbolExists(ord.getSymbol())) {
			symbols.get(ord.getSymbol()).removeOrd(ord);
			if (symbols.get(ord.getSymbol()).getTotaNumberOfLimits() <= 0) {
				symbols.remove(ord.getSymbol());
			}
		} else {
			System.err.println("Symbol doesn't exist in orderbook!");
		}
	}

	// TODO Exec report when creating a new symb
	private void createNewSymbol(String pSymName) {
		Symbol newSymbol = new Symbol(pSymName);
		symbols.put(newSymbol.getSymbolName(), newSymbol);
	}

	private boolean checkSymbolExists(String pSymName) {
		if (symbols.containsKey(pSymName)) {
			return true;
		} else {
			return false;
		}
	}

	// public HashMap getPrices() {
	// HashMap<String, Double[]> prices = new HashMap<String, Double[]>();
	// for (Map.Entry<String, Symbol> e : symbols.entrySet()) {
	// // prices.put(e.getKey(), e.getValue().getBestBidBestAsk());
	// }
	// return prices;
	// }

}
