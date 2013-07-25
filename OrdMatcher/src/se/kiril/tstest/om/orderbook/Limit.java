package se.kiril.tstest.om.orderbook;

import java.util.Iterator;
import java.util.LinkedList;

import se.kiril.tstest.om.enums.ExecType;
import se.kiril.tstest.om.reports.ExecutionReport;

public class Limit {
	private double price;
	private int totalQty;

	// private LinkedHashMap<String, Order> orders = new LinkedHashMap<String,
	// Order>();
	private LinkedList<Order> orders = new LinkedList<Order>();

	public Limit(double pPrice) {
		this.price = pPrice;
	}

	//Debug 
	public LinkedList<Order> getOrders(){
		return orders;
	}
	public Order tradeFromInsideOfLimit(Order pOrd) {
		Order initOrd = pOrd;
		for (Order order : orders) {
			if (initOrd != null && initOrd.getLeavesQty() > 0) {
				initOrd = order.trade(initOrd, price, initOrd.leavesQty);
			} else {
				break;
			}
		}
		recalcSize();
		clearEmptyOrders();
		return initOrd;
	}

	private void clearEmptyOrders() {
		for (Iterator<Order> it = orders.iterator(); it.hasNext();) {
			if (it.next().getQty() <= 0) {
				it.remove();
			}
		}
	}

	public ExecutionReport addOrderToLimit(Order ord) {
		orders.add(ord);
		addToSize(ord.getLeavesQty());
		ExecutionReport report = new ExecutionReport(ExecType.NEW, ord);
		return report;
	}

	public void removeOrderFromLimit(Order pOrder) {
		orders.remove(pOrder);
		recalcSize();
	}

	public double getPrice() {
		return price;
	}

	public int getSize() {
		return totalQty;
	}

	private void addToSize(int pVol) {
		this.totalQty += pVol;
	}

	private void recalcSize() {
		int tq = 0;
		for (Order order : orders) {
			tq += order.getLeavesQty();
		}
		totalQty = tq;
	}
}
