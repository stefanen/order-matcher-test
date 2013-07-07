package se.kiril.ob.orderbook;

//t
import java.util.Calendar;
import java.util.Random;

import se.kiril.ob.enums.ExecType;
import se.kiril.ob.enums.OrdStatus;
import se.kiril.ob.enums.OrdType;
import se.kiril.ob.enums.Side;
import se.kiril.ob.reports.ExecutionReport;

public class Order {
	private static volatile int ordSeqNr = 1;
	private final String symbol;
	private final Side side;
	private final OrdType ordType;
	private final double limit;
	private double lastFillPrice;
	private int ordQty;
	private final String user;
	private final long entryTime;
	
	protected OrdStatus ordStatus;
	// private final long eventTime; (Execution report time)
	private final String ordId;
	private int leavesQty;
	private int cumQty;

	public Order(String pSymbol, Side pSide, OrdType pType, double pLimit,
			int pQty, String pUser) {

		symbol = pSymbol;
		side = pSide;
		ordType = pType;
		limit = pLimit;
		ordQty = pQty;
		user = pUser;
		entryTime = createTimestamp();
		ordId = createOrdId(entryTime, side);
		ordSeqNr++;
		cumQty = 0;
		leavesQty= ordQty;
		ordStatus = OrdStatus.NEW;
	}

	public ExecutionReport generateExecReport(ExecType execType) {
		ExecutionReport report = new ExecutionReport(execType, this);
		return report;
	}

	private String createOrdId(long pOrdTimestamp, Side pSide) {
		Random rand = new Random();
		int r = rand.nextInt(999 - 100) + 100;
		String id = ordType.toString() + "-" + String.valueOf(pOrdTimestamp)
				+ "-" + r + "-" + ordSeqNr + "-" + String.valueOf(pSide);
		return id;
	}

	private long createTimestamp() {
		Calendar c = Calendar.getInstance();
		long now = c.getTimeInMillis();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		long msSinceMidnight = now - c.getTimeInMillis();
		return msSinceMidnight;
	}

	public ExecutionReport trade(Double pPrice, int pVol) {
		if (pVol <= leavesQty){
			leavesQty -= pVol;
			cumQty += pVol;
			lastFillPrice = pPrice;
			ExecutionReport report = new ExecutionReport(ExecType.TRADE, this);			
			return report;
		}else{
			System.err.println("Invalid trade qty during execution!");
			return null;
		}
	}

	public void reduceQty(int pVol) {
		ordQty -= pVol;
	}

	public String getOrdId() {
		return ordId;
	}

	public long getEntryTime() {
		return entryTime;
	}

	public String getSymbol() {
		return symbol;
	}

	public Side getSide() {
		return side;
	}

	public double getLimit() {
		return limit;
	}

	public int getQty() {
		return ordQty;
	}

	public String getUser() {
		return user;
	}

	public void setQty(int pQty) {
		ordQty = pQty;
	}

	public OrdType getOrdType() {
		return ordType;
	}

	public int getLeavesQty() {
		return leavesQty;
	}

	public int getCumQty() {
		return cumQty;
	}
	public OrdStatus getOrdStatus(){
		return ordStatus;
	}

	public double getLastFillPrice() {
		return lastFillPrice;
	}


}