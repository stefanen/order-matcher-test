package se.kiril.qftest.fix.server;
import java.io.*;
import java.util.*;


// Quickfix Exceptions
import quickfix.DoNotSend;
import quickfix.FieldConvertError;
import quickfix.ConfigError;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.FieldNotFound;
import quickfix.UnsupportedMessageType;
import quickfix.SessionNotFound;
import quickfix.RejectLogon;

// Quickfix Data Types
import quickfix.SessionID;
import quickfix.Message;
import quickfix.Session;
import quickfix.SessionSettings;
import quickfix.FixVersions;
import quickfix.MessageUtils;
import quickfix.field.*;
import quickfix.fix42.ExecutionReport;
import quickfix.fix42.OrderStatusRequest;

public class Application extends quickfix.MessageCracker implements
		quickfix.Application {
	private MarketData md = null;
	private Map<String, Double> priceMap = null;
	
	public Application(SessionSettings settings) throws ConfigError,
			FieldConvertError {
		md = new MarketData();
		String ask = md.getAsk("0001");
		System.out.println(ask);
		
		priceMap = new HashMap<String, Double>();
		priceMap.put("EUR/USD", 1.234);
	}

	// ===================================================
	// Overriden quickfix.MessageCracker onMessage()
	// ===================================================
	
	
	// added trade func
	public void onMessage(quickfix.fix42.NewOrderSingle order,
			SessionID sessionID) throws FieldNotFound, UnsupportedMessageType,
			IncorrectTagValue {
		
		OrdType orderType = order.getOrdType();
		Symbol currencyPair = order.getSymbol();
		
		Price price = null;
		if (OrdType.FOREX_MARKET == orderType.getValue()){
			if(this.priceMap.containsKey(currencyPair.getValue())){
				price = new Price(this.priceMap.get(currencyPair.getValue()));
			}else{
				price = new Price(1.4589);
			}
		}
		OrderID orderId = new OrderID("1");
		ExecID execId = new ExecID("1");
		ExecTransType executionTransactionType = new ExecTransType(ExecTransType.NEW);
		ExecType purposeOfExecutionReport = new ExecType(ExecType.FILL);
		OrdStatus orderStatus = new OrdStatus(OrdStatus.FILLED);
		Symbol symbol = currencyPair;
		Side side = order.getSide();
		LeavesQty leavesQty = new LeavesQty(100);
		CumQty totalQty = new CumQty(100);
		AvgPx avgPx = new AvgPx(1.235);
		
		ExecutionReport execReport = new ExecutionReport(orderId, execId, executionTransactionType, 
				purposeOfExecutionReport, orderStatus, symbol, side, leavesQty, totalQty, avgPx);
		execReport.set(price);
		
		try{
			Session.sendToTarget(execReport, sessionID);
		}catch (SessionNotFound e){
			e.printStackTrace();
		}
		
	}

	// ===================================================
	// quickfix.Application interface implementation
	// ===================================================
	// Called when quickfix creates a new session
	public void onCreate(SessionID sessionID) {
		System.out.println("===============================");
		System.out.println("New session created with id: "+sessionID);
		System.out.println("===============================");
	}	
	// notification that a valid logon has been established
	// with the counter party
	public void onLogon(SessionID sessionID) {
	}

	// notification that a FIX session is no longer online
	public void onLogout(SessionID sessionID) {
		System.out.println("===============================");
		System.out.println("Session Ended: "+sessionID);
		System.out.println("===============================");
	}
		
	// allows for peaking at msgs from this apps FIX engine to
	// the counter party
	public void toAdmin(Message msg, SessionID sessionID) {
	}

	// callback notify for when admin msgs are received by FIX from
	// the counter party
	public void fromAdmin(Message msg, SessionID sessionID)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
			RejectLogon {
	}

	// callback for app messages this app send to the counter party
	public void toApp(Message msg, SessionID sessionID) throws DoNotSend {
	}

	// all app level requests comes through here
	public void fromApp(Message msg, SessionID sessionID) throws FieldNotFound,
			IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		// call base class message parser
		crack(msg, sessionID);
	}
}