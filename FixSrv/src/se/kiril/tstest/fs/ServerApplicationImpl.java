package se.kiril.tstest.fs;

import java.util.LinkedList;

import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.UnsupportedMessageType;
import quickfix.field.AvgPx;
import quickfix.field.CumQty;
import quickfix.field.ExecID;
import quickfix.field.ExecTransType;
import quickfix.field.ExecType;
import quickfix.field.LeavesQty;
import quickfix.field.OrdStatus;
import quickfix.field.OrdType;
import quickfix.field.OrderID;
import quickfix.field.OrderQty;
import quickfix.field.Side;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.Logon;
import quickfix.fix44.MessageCracker;
import quickfix.fix44.NewOrderSingle;
import quickfix.fix44.SecurityDefinition;
import se.kiril.tstest.fs.commons.FixOrder;


public class ServerApplicationImpl extends MessageCracker implements Application {
	protected LinkedList<NewOrderSingle> fixOrdsQ = new LinkedList<>();
	@Override
	public void fromAdmin(Message arg0, SessionID arg1) throws FieldNotFound,
			IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fromApp(Message message, SessionID sessionId) throws FieldNotFound,
			IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		// TODO Auto-generated method stub
		 crack(message, sessionId);
	}

	@Override
	public void onCreate(SessionID sessionId) {
		// TODO Auto-generated method stub
		System.out.println("Executor Session Created with SessionID = " + sessionId);
	}

	@Override
	public void onLogon(SessionID sessionId) {
		// TODO Auto-generated method stub
		System.out.println("Logon SUCCESS on session " +sessionId);
	}

	@Override
	public void onLogout(SessionID sessionId) {
		// TODO Auto-generated method stub
		System.out.println("Logout on session " +sessionId);
	}

	@Override
	public void toAdmin(Message arg0, SessionID arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toApp(Message arg0, SessionID arg1) throws DoNotSend {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onMessage(NewOrderSingle message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
		// TODO Validate message with Account persistence
		// If message is rejected return an exec report with OrdStatus=REJECTED
		// Assuming a valid message:
		// Add message to an outgoing queue
		// 
		fixOrdsQ.add(message);
		
		// Return an exec report to sender
		//Need to add some IDs
		OrderID ordId = new OrderID("1");
		ExecID execId = new ExecID("1");
		
		ExecType execType = new ExecType(ExecType.NEW);
		OrdStatus ordStatus = new OrdStatus(OrdStatus.PENDING_NEW);
		Side side = message.getSide();
		OrderQty orderQty = message.getOrderQty();
		LeavesQty leavesQty = new LeavesQty(orderQty.getValue());
		CumQty cumQty = new CumQty(0);
		AvgPx avgPx = new AvgPx(0);
		
		ExecutionReport executionReport = new ExecutionReport(ordId, execId, execType, ordStatus, side, leavesQty, cumQty, avgPx);
		executionReport.set(message.getSymbol());
		try {
            Session.sendToTarget(executionReport,sessionID);
        } catch (SessionNotFound e) {
            e.printStackTrace();
        }
	}
	
}
