package se.kiril.ob.orderbook;

import java.util.Iterator;
import java.util.LinkedList;

import se.kiril.ob.enums.ExecType;
import se.kiril.ob.reports.ExecutionReport;

public class Limit {
	private double price;
	private int totalQty;

	private LinkedList<Order> orders = new LinkedList<Order>();

	public Limit(double pPrice) {
		this.price = pPrice;
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
		orders.add(ord);
		addToSize(ord.getQty());
		return ord.generateExecReport(ExecType.NEW);
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
