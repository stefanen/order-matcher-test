package se.kiril.tstest.om.symbols;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import se.kiril.tstest.om.enums.ExecType;
import se.kiril.tstest.om.enums.Side;
import se.kiril.tstest.om.orderbook.Limit;
import se.kiril.tstest.om.orderbook.Order;
import se.kiril.tstest.om.reports.ExecutionReport;

public class SymbolSide implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1652717513509806385L;
	protected final Side side;
	protected TreeMap<Double, Limit> limits = new TreeMap<Double, Limit>();
	protected List<Order> marketOrdsQueue = new LinkedList<Order>();

	protected Double bestLimit = 0.0;

	public SymbolSide(Side pSide) {
		side = pSide;
	}
	
	//Debug only 
	public String getSide(){
		if (side == Side.ASK){
			return "SELL";
		}else {
			return "BUY";
		}
	}
	//Debug only
	public List<Order> getMarketOrds(){
		return marketOrdsQueue;
	}
	//Debug only
	public TreeMap<Double, Limit> getLimitsMap(){
		return limits;
	}
	private void removeLimit(double limitPrice) {
		if (limits.get(limitPrice).getSize() == 0) {
			limits.remove(limitPrice);
			refreshBestLimit();
		}
	}

	private void createLimit(double limitPrice) {
		Limit lim = new Limit(limitPrice);
		limits.put(limitPrice, lim);
		refreshBestLimit();
	}

	private void refreshBestLimit() {
		if (side == Side.ASK) {
			bestLimit = limits.firstKey();
		} else if (side == Side.BID) {
			bestLimit = limits.lastKey();
		}
	}

	public Order execLimOrd(Order pOrd) {
		Order initOrd = pOrd;
		if (side == Side.ASK) {
			for (Map.Entry<Double, Limit> limit : limits.entrySet()) {
				Double key = limit.getKey();
				Limit value = limit.getValue();
				Double initOrdLimPx = initOrd.getLimitPx();
				if (initOrdLimPx >= key) {
					initOrd = value.tradeFromInsideOfLimit(initOrd);
				} else {
					break;
				}
				if (initOrd == null) {
					break;
				}
			}
			return initOrd;
		} else if (side == Side.BID) {
			NavigableMap<Double, Limit> dLimits = limits.descendingMap();
			for (NavigableMap.Entry<Double, Limit> limit : dLimits.entrySet()) {
				Double key = limit.getKey();
				Limit value = limit.getValue();
				Double initOrdLimPx = initOrd.getLimitPx();
				if (initOrdLimPx <= key) {
					initOrd = value.tradeFromInsideOfLimit(initOrd);
				} else {
					break;
				}
				if (initOrd == null) {
					break;
				}
			}
			return initOrd;
		} else {
			return null;
		}
	}

	public ExecutionReport addLimitOrd(Order ord) {
		if (limits.containsKey(ord.getLimitPx())) {
			return limits.get(ord.getLimitPx()).addOrderToLimit(ord);
		} else {
			createLimit(ord.getLimitPx());
			refreshBestLimit();
			return limits.get(ord.getLimitPx()).addOrderToLimit(ord);
		}
	}

	public ExecutionReport addMarketOrd(Order ord) {
		marketOrdsQueue.add(ord);
		ExecutionReport report = new ExecutionReport(ExecType.NEW, ord);
		return report;
	}

	public ExecutionReport addStopLossOrd(Order ord) {
		return null;
	}

	public ExecutionReport addStopLimitOrd(Order ord) {
		return null;
	}

	public ExecutionReport addPeggedOrd(Order ord) {
		return null;
	}

	public ExecutionReport addAllOrNothingOrd(Order ord) {
		return null;
	}

	public ExecutionReport addFillOrKillOrd(Order ord) {
		return null;
	}

	public int getNumberOfLimits() {
		return limits.size();
	}

	public Double getBestLimit() {
		return bestLimit;
	}
}
