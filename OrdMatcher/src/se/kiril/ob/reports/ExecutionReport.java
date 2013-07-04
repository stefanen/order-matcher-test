package se.kiril.ob.reports;

import java.util.Calendar;
import java.util.Random;

import se.kiril.ob.enums.ExecType;
import se.kiril.ob.enums.OrdStatus;
import se.kiril.ob.enums.OrdType;
import se.kiril.ob.enums.Side;
import se.kiril.ob.orderbook.Order;

//confirm the receipt of an order
//confirm changes to an existing order (i.e. accept cancel and replace requests)
//relay order status information
//relay fill information on working orders
//relay fill information on tradeable or restricted tradeable quotes
//reject orders
//report post-trade fees calculations associated with a trade
public class ExecutionReport {
	private static volatile int reportSeqNr= 1;
	
	private String execId;  // identifier for exec message
	private final ExecType execType; // purpose of this report
	private final long transactTime;

	private String ordId; // order identifier
	private OrdStatus ordStatus; // current status of the order
	private String symbol;
	private Side side;
	private OrdType ordType;
	private int ordQty; 
	private int leavesQty;//Qty open for further execution
	private int cumQty; // total quantity executed so far for a chain of orders
	
	//private double avgPx; // average price of all fills on this order
	
	public ExecutionReport(ExecType pExecType, Order ord){
		execType = pExecType;
		ordId=ord.getOrdId();
		symbol=ord.getSymbol();
		side=ord.getSide();
		ordType=ord.getOrdType();
		ordQty=ord.getQty();
		leavesQty=ord.getLeavesQty();
		cumQty=ord.getCumQty();
		
		transactTime = createTimestamp();
		execId = createExecId(transactTime);
		reportSeqNr++;
	}
	private long createTimestamp(){
		Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long msSinceMidnight = now - c.getTimeInMillis();
        return msSinceMidnight;
	}
	private String createExecId(long pExecTimestamp){
		Random rand = new Random();
        int r = rand.nextInt(999-100)+100;
        String id = execType.toString()+"-"+String.valueOf(pExecTimestamp)+"-"+r+"-"+reportSeqNr;
        return id;
	}
	
	
	public ExecType getExecType() {
		return execType;
	}
	public long getTransactTime() {
		return transactTime;
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

}
