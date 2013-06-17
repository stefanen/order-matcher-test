package se.kiril.matcher.commons;
//t
import java.util.Calendar;
import java.util.Random;

// timestamp, id, symbol, side, limit, quantity, user
public class Order {
	private static volatile int ordSeqNr= 1;

	private final String symbol;
	private final char side;
	private final double limit;
	private int qty;
	private final String user;
	private final long entryTime;
    //private final long eventTime;
	private final String ordId;

	public Order(String pSymbol, char pSide, double pLimit, int pQty, String pUser){
		symbol = pSymbol;
		side = pSide;
		limit = pLimit;
		qty = pQty;
		user = pUser;
		entryTime = createTimestamp();
		ordId = createOrdId(entryTime, side);
		ordSeqNr++;
	}
	private String createOrdId(long pOrdTimestamp, char pSide){
		Random rand = new Random();
		int r = rand.nextInt(999-100)+100;
		String id = "L-"+String.valueOf(pOrdTimestamp)+"-"+r+"-"+ordSeqNr+"-"+String.valueOf(pSide);
		return id;
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
	public String getOrdId(){
		return ordId;
	}
	public long getEntryTime(){
		return entryTime;
	}
	public String getSymbol() {
		return symbol;
	}
	public char getSide() {
		return side;
	}
	public double getLimit() {
		return limit;
	}
	public int getQty() {
		return qty;
	}
	public String getUser() {
		return user;
	}
	public void setQty(int pQty){
		qty = pQty;
	}
}
