package se.kiril.ob.reports;

import se.kiril.ob.enums.ExecType;
import se.kiril.ob.enums.OrdStatus;

//confirm the receipt of an order
//confirm changes to an existing order (i.e. accept cancel and replace requests)
//relay order status information
//relay fill information on working orders
//relay fill information on tradeable or restricted tradeable quotes
//reject orders
//report post-trade fees calculations associated with a trade
public class ExecutionReport {
	private String orderId; // order identifier
	private String execId;  // identifier for exec message
	private ExecType execType; // purpose of this report
	private OrdStatus ordStatus; // current status of the order
	
	//Instrument
	private String symbol;
	
	private char side;
	
	//OrderQtyData
	private int orderQty; 
	
	private int leavesQty;//Qty open for further execution
	private int cumQty; // total quantity executed so far for a chain of orders
	
	//private double avgPx; // average price of all fills on this order
	
	public ExecutionReport(){

	}
}
