package se.kiril.tstest.fc.commons;

//import java.sql.Date;
//import java.sql.Time;

public class FixOrder {
	private static volatile int ordSeqNr = 0;

	private final String symbol;
	private final char side;
	private final String user;
	protected final char ordType;
	private final Double limitPx;
	// private final long entryTime;
	// protected final String ordId;
	// private final Date expireDate;
	// private final Time expireTime;

	private final int ordQty;

	protected int leavesQty;
	protected int cumQty;
	protected int lastQty;

	protected double lastPx;
	protected double grossTradeAmt;
	protected char ordStatus;

	public FixOrder(String pSymbol, char pSide, char pType, double pLimit,
			int pQty, String pUser) {
		symbol = pSymbol;
		side = pSide;
		user = pUser;
		ordType = pType;
		limitPx = pLimit;
		ordQty = pQty;
		ordSeqNr++;
	}

	/**
	 * Trades the initiator order pOrd with the target order producing two
	 * execution reports.
	 * 
	 * @param pOrd
	 *            Initiator order
	 * @param pLimitPrice
	 *            trade price
	 * @param pVol
	 *            trade volume
	 * @return Returns an object array where [0]=Modified initiator order,
	 *         [1]=Initiator order execution report, [2]=Target order execution
	 *         report
	 */

	// public String getOrdId() {return ordId;}
	// public long getEntryTime() {return entryTime;}
	public String getSymbol() {
		return symbol;
	}

	public char getSide() {
		return side;
	}

	public Double getLimitPx() {
		return limitPx;
	}

	public int getQty() {
		return ordQty;
	}

	public String getUser() {
		return user;
	}

	public char getOrdType() {
		return ordType;
	}

	public int getLeavesQty() {
		return leavesQty;
	}

	public int getCumQty() {
		return cumQty;
	}

	public char getOrdStatus() {
		return ordStatus;
	}

	public double getLastPx() {
		return lastPx;
	}

	public int getLastQty() {
		return lastQty;
	}

	public double getGrossTradeAmt() {
		return grossTradeAmt;
	}

	public Integer getOrdSeqNr() {
		return ordSeqNr;
	}
}