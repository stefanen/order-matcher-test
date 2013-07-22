package se.kiril.tstest.om.reports;

import java.util.Calendar;
import java.util.Random;

import se.kiril.tstest.om.enums.ExecType;
import se.kiril.tstest.om.enums.OrdStatus;
import se.kiril.tstest.om.enums.OrdType;
import se.kiril.tstest.om.enums.Side;
import se.kiril.tstest.om.orderbook.Order;

/**
 * @author krl - confirm the receipt of an order - confirm changes to an
 *         existing order (i.e. accept cancel and replace requests) - relay
 *         order status information - relay fill information on working orders -
 *         relay fill information on tradeable or restricted tradeable quotes -
 *         reject orders - report post-trade fees calculations associated with a
 *         trade
 */
public class ExecutionReport {
	private static volatile int reportSeqNr = 1;

	private final String execId; // identifier for exec message
	private final ExecType execType; // purpose of this report
	private final long reportTime;

	private final String ordId; // order identifier
	private final OrdStatus ordStatus; // current status of the order
	private final String symbol;
	private final Double grossTradeAmt;
	private final Double limitPx;
	private final Double lastPx;
	private final Side side;
	private final OrdType ordType;
	private final int ordQty;
	private final int leavesQty;// Qty open for further execution
	private final int cumQty; // total quantity executed so far for a chain of
								// orders
	private final int lastQty;

	public ExecutionReport(ExecType pExecType, Order ord) {
		execType = pExecType;
		ordType = ord.getOrdType();
		ordId = ord.getOrdId();
		symbol = ord.getSymbol();
		side = ord.getSide();
		ordQty = ord.getQty();
		leavesQty = ord.getLeavesQty();
		cumQty = ord.getCumQty();
		lastQty = ord.getLastQty();
		limitPx = ord.getLimitPx();
		lastPx = ord.getLastPx();
		ordStatus = ord.getOrdStatus();
		reportTime = createTimestamp();
		grossTradeAmt = ord.getGrossTradeAmt();
		execId = createExecId(reportTime);
		reportSeqNr++;
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

	private String createExecId(long pExecTimestamp) {
		Random rand = new Random();
		int r = rand.nextInt(999 - 100) + 100;
		String id = execType.toString() + "-" + String.valueOf(pExecTimestamp)
				+ "-" + r + "-" + reportSeqNr;
		return id;
	}

	public ExecType getExecType() {
		return execType;
	}

	public long getReportTime() {
		return reportTime;
	}

	public String getOrdId() {
		return ordId;
	}

	public String getSymbol() {
		return symbol;
	}

	public int getOrdQty() {
		return ordQty;
	}

	public int getCumQty() {
		return cumQty;
	}

	public OrdType getOrdType() {
		return ordType;
	}

	public Side getSide() {
		return side;
	}

	public OrdStatus getOrdStatus() {
		return ordStatus;
	}

	public String getExecId() {
		return execId;
	}

	public int getLeavesQty() {
		return leavesQty;
	}

	public Double getLimitPrice() {
		return limitPx;
	}

	public Double getLastPx() {
		return lastPx;
	}

	public Double getGrossTradeAmt() {
		return grossTradeAmt;
	}

	public int getLastQty() {
		return lastQty;
	}
}
