package se.kiril.ob.symbols;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import se.kiril.ob.enums.ExecType;
import se.kiril.ob.enums.Side;
import se.kiril.ob.orderbook.Limit;
import se.kiril.ob.orderbook.Order;
import se.kiril.ob.reports.ExecutionReport;


public class SymbolSide {
	protected final Side side;
	protected TreeMap<Double, Limit> limits = new TreeMap<Double, Limit>();
	protected List<Order> marketOrdsQueue = new LinkedList<Order>();
	
	protected Double bestLimit;

	
	public SymbolSide(Side pSide){
		side = pSide;
	}
	private void removeLimit(double limitPrice){
		if(limits.get(limitPrice).getSize() == 0){
			limits.remove(limitPrice);
			refreshBestLimit(side);
		}
	}
	private void createLimit(double limitPrice){
		Limit lim = new Limit(limitPrice);
		limits.put(limitPrice, lim);
		refreshBestLimit(side);
	}
	private void refreshBestLimit(Side side){
		if (side == Side.ASK){
			bestLimit = limits.firstKey();
		}else if (side == Side.BID){
			bestLimit = limits.lastKey();
		}
	}
	
	
	public ExecutionReport addLimitOrd(Order ord){
		if(limits.containsKey(ord.getLimit())){
			return limits.get(ord.getLimit()).addOrderToLimit(ord);
		}else{
			createLimit(ord.getLimit());
			return limits.get(ord.getLimit()).addOrderToLimit(ord);
		}
	}
	public ExecutionReport addMarketOrd(Order ord){
		marketOrdsQueue.add(ord);
		return ord.generateExecReport(ExecType.NEW);
	}
	public ExecutionReport addStopLossOrd(Order ord){
		return null;
	}
	public ExecutionReport addStopLimitOrd(Order ord){
		return null;
	}
	public ExecutionReport addPeggedOrd(Order ord){
		return null;
	}
	public ExecutionReport addAllOrNothingOrd(Order ord){
		return null;
	}
	public ExecutionReport addFillOrKillOrd(Order ord){
		return null;
	}
	public int getNumberOfLimits(){
		return limits.size();
	}
	public Double getBestLimit(){
		return bestLimit;
	}
}
