package se.kiril.tstest.om.orderbook;

//import java.sql.Date;
//import java.sql.Time;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Random;

import se.kiril.tstest.om.enums.ExecType;
import se.kiril.tstest.om.enums.OrdStatus;
import se.kiril.tstest.om.enums.OrdType;
import se.kiril.tstest.om.enums.Side;
import se.kiril.tstest.om.reports.ExecutionReport;

public class Order implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9149708730169565557L;

	private static volatile int ordSeqNr = 1;

	private final String symbol;
	private final Side side;
	private final String account;
	protected final OrdType ordType;
	private final Double limitPx;
	private final long entryTime;
	protected final String ordId;
	// private final Date expireDate;
	// private final Time expireTime;

	private final int ordQty;

	protected int leavesQty;
	protected int cumQty;
	protected int lastQty;

	protected double lastPx;
	protected double grossTradeAmt;
	protected OrdStatus ordStatus;

	public Order(String pSymbol, char pSide, char pType, Double pLimit,
			Double pQty, String pAcc) {
		symbol = pSymbol;
		// TODO Make this more flexible
		if (pSide == '1') {
			side = Side.BID;
		} else if (pSide == '2') {
			side = Side.ASK;
		} else {
			side = Side.BID;
		}
		// side = pSide;
		account = pAcc;
		if (pType == '2') {
			ordType = OrdType.LIMIT;
		} else {
			ordType = OrdType.MARKET;
		}
		// ordType = pType;
		limitPx = pLimit;
		ordQty = pQty.intValue();
		entryTime = createTimestamp();
		ordId = createOrdId(entryTime, side);
		ordSeqNr++;

		leavesQty = ordQty;
		cumQty = 0;
		lastQty = 0;
		lastPx = 0;
		grossTradeAmt = 0.0;
		ordStatus = OrdStatus.NEW;
	}

	public ExecutionReport cancelOrd() {
		ordStatus = OrdStatus.CANCELED;
		ExecutionReport report = new ExecutionReport(ExecType.CANCELED, this);
		return report;
	}

	public ExecutionReport triggerEndOfDay() {
		ordStatus = OrdStatus.DONE_FOR_DAY;
		ExecutionReport report = new ExecutionReport(ExecType.DONE_FOR_DAY,
				this);
		return report;
	}

	public ExecutionReport queryOrdStatus() {
		ExecutionReport report = new ExecutionReport(ExecType.ORDER_STATUS,
				this);
		return report;
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
	public Order trade(Order pOrd, Double pLimitPrice, int pVol) {
		Order initOrder = pOrd;
		if (ordStatus.equals(OrdStatus.NEW)
				|| ordStatus.equals(OrdStatus.PARTIALLY_FILLED)) {
			if (pVol <= leavesQty) {
				leavesQty -= pVol;
				cumQty += pVol;
				lastQty = pVol;
				lastPx = pLimitPrice;
				grossTradeAmt += lastPx * lastQty;
				if (leavesQty <= 0) {
					ordStatus = OrdStatus.FILLED;
				} else {
					ordStatus = OrdStatus.PARTIALLY_FILLED;
				}
				initOrder.leavesQty -= pVol;
				initOrder.cumQty += pVol;
				initOrder.lastQty = pVol;
				initOrder.lastPx = pLimitPrice;
				initOrder.grossTradeAmt += initOrder.lastPx * initOrder.lastQty;
				if (initOrder.leavesQty <= 0) {
					initOrder.ordStatus = OrdStatus.FILLED;
				} else {
					initOrder.ordStatus = OrdStatus.PARTIALLY_FILLED;
				}
				ExecutionReport ordReport = new ExecutionReport(ExecType.TRADE,
						this);
				ExecutionReport initiatorOrdReport = new ExecutionReport(
						ExecType.TRADE, initOrder);

				broadcastExecReport(initiatorOrdReport);
				broadcastExecReport(ordReport);
				return initOrder;
			} else {
				initOrder.lastQty = leavesQty;
				initOrder.leavesQty -= leavesQty;
				initOrder.cumQty += leavesQty;
				initOrder.lastPx = pLimitPrice;
				initOrder.grossTradeAmt += initOrder.lastQty
						* initOrder.leavesQty;
				if (initOrder.leavesQty <= 0) {
					initOrder.ordStatus = OrdStatus.FILLED;
				} else {
					initOrder.ordStatus = OrdStatus.PARTIALLY_FILLED;
				}
				cumQty += leavesQty;
				lastPx = pLimitPrice;
				lastQty = leavesQty;
				leavesQty = 0;
				grossTradeAmt += lastPx * lastQty;
				ordStatus = OrdStatus.FILLED;
				ExecutionReport ordReport = new ExecutionReport(ExecType.TRADE,
						this);
				ExecutionReport initiatorOrdReport = new ExecutionReport(
						ExecType.TRADE, initOrder);

				broadcastExecReport(initiatorOrdReport);
				broadcastExecReport(ordReport);
				return initOrder;
			}
		} else {
			// System.err.println("Cannot trade with "+this.ordId+", the order is "+
			// ordStatus.toString());
			return null;
		}
	}

	private void broadcastExecReport(ExecutionReport report) {
		// TODO
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

	public Double getLimitPx() {
		return limitPx;
	}

	public int getQty() {
		return ordQty;
	}

	public String getUser() {
		return account;
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

	public OrdStatus getOrdStatus() {
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
}