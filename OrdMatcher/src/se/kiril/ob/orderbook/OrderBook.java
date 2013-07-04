package se.kiril.ob.orderbook;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import se.kiril.ob.symbols.Symbol;

public class OrderBook {

	private Map<String, Symbol> symbols = new LinkedHashMap<String, Symbol>();
	@Deprecated
	private Map<String, Order> ordersMap = new HashMap<String, Order>();

	public OrderBook() {

	}

	// TODO get total costs from executions (in some sort of exec report)
	public void addOrder(Order ord) {
		if (checkSymbolExists(ord.getSymbol())) {
			symbols.get(ord.getSymbol()).addOrd(ord);

			ordersMap.put(ord.getOrdId(), ord);
		} else {
			createNewSymbol(ord.getSymbol());
			symbols.get(ord.getSymbol()).addOrd(ord);
			ordersMap.put(ord.getOrdId(), ord);
		}

	}

	public void removeOrder(String ordId) {
		removeOrderFromOb(ordersMap.get(ordId));
		ordersMap.remove(ordId);
	}

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

	public HashMap getPrices() {
		HashMap<String, Double[]> prices = new HashMap<String, Double[]>();
		for (Map.Entry<String, Symbol> e : symbols.entrySet()) {
			// prices.put(e.getKey(), e.getValue().getBestBidBestAsk());
		}
		return prices;
	}

}
