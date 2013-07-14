package se.kiril.ob.orderbook;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import se.kiril.ob.enums.ExecType;
import se.kiril.ob.enums.OrdStatus;
import se.kiril.ob.reports.ExecutionReport;
import se.kiril.ob.symbols.SymbolSide;

public class Limit {
	private double price;
	private int totalQty;

	//private LinkedHashMap<String, Order> orders = new LinkedHashMap<String, Order>();
	private LinkedList<Order> orders = new LinkedList();
	
	public Limit(double pPrice) {
		this.price = pPrice;
	}

	
	public ExecutionReport tradeFromInsideOfLimit(Order pOrd){
		Order initOrd = pOrd;
		for (Order order : orders){
			if (initOrd.getLeavesQty() > 0){
				ExecutionReport cpReport = order.trade(price, initOrd.leavesQty);				
				initOrd.leavesQty -= cpReport.getLastQty();
				initOrd.cumQty += cpReport.getLastQty();
				initOrd.lastQty = cpReport.getLastQty();
				initOrd.lastPx = cpReport.getLastPx();
				initOrd.grossTradeAmt += initOrd.lastPx* initOrd.lastQty;
				if (initOrd.leavesQty <= 0){
					initOrd.ordStatus = OrdStatus.FILLED;
				}else{
					initOrd.ordStatus = OrdStatus.PARTIALLY_FILLED;
				}
			}else{
				break;
			}
		}
		ExecutionReport report = new ExecutionReport(ExecType.TRADE, initOrd);
		return report;
	}
	
	public int popFromInsideOfLimit(int vol) {
		int tradedQty = 0;
		int remainingVol = vol;
		for (Order order : orders) {
			if (remainingVol > 0) {
				int tVol = 0;
				tVol = order.trade(remainingVol);
				tradedQty += tVol;
				remainingVol -= tVol;
			} else {
				break;
			}
		}
		clearEmptyOrders();
		removeFromSize(tradedQty);
		return tradedQty;
	}

	private void clearEmptyOrders() {
		for (Iterator<Order> it = orders.iterator(); it.hasNext();) {
			if (it.next().getQty() <= 0) {
				it.remove();
			}
		}
	}

	public ExecutionReport addOrderToLimit(Order ord) {
		orders.put(ord.getOrdId(), ord);
		addToSize(ord.getLeavesQty());	
		ExecutionReport report = new ExecutionReport(ExecType.NEW, ord);
		return report;
	}
	
	
	public ExecutionReport tradeOrd(Order pOrd, Double pPrice, int pVol){
		return orders.get(pOrd.getOrdId()).trade(pPrice, pVol);
	}

	public void removeOrderFromLimit(Order pOrder) {
		orders.remove(pOrder);
		removeFromSize(pOrder.getQty());

	}

	// private void reduceOrder(Order pOrder, int pVol){
	// if (orders.contains(pOrder)){
	// pOrder.reduceQty(pVol);
	// if (pOrder.getQty() <= 0){
	// removeOrderFromLimit(pOrder);
	// }
	// }
	// }

	public double getPrice() {
		return price;
	}

	public int getSize() {
		return totalQty;
	}

	private void addToSize(int pVol) {
		this.totalQty += pVol;
	}

	public void removeFromSize(int pVol) {
		this.totalQty -= pVol;
	}
}
