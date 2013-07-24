package se.kiril.tstest.fs.fix.session;

import java.awt.Event;
import java.util.LinkedList;

import quickfix.FieldNotFound;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.UnsupportedMessageType;
import quickfix.field.AvgPx;
import quickfix.field.CumQty;
import quickfix.field.ExecID;
import quickfix.field.ExecType;
import quickfix.field.LeavesQty;
import quickfix.field.OrdStatus;
import quickfix.field.OrderID;
import quickfix.field.OrderQty;
import quickfix.field.Side;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.NewOrderSingle;
import se.kiril.tstest.fs.commons.OrderListenerIf;
import se.kiril.tstest.fs.fix.connection.ConnectionFix;

public class FsConnectionFix extends ConnectionFix {

	private static volatile int seqNr = 1;
	private boolean listenerSet = false;
	private OrderListenerIf ordListener;
	protected LinkedList<NewOrderSingle> receivedOrdsQueue = new LinkedList<NewOrderSingle>();

	public void setListener(OrderListenerIf lst) {
		this.ordListener = lst;
		listenerSet = true;
	}

	public NewOrderSingle getOldestInQueue() {
		return receivedOrdsQueue.getFirst();
	}

	public void removeOldestInQueue() {
		receivedOrdsQueue.removeFirst();
	}

	public int getQueueSize() {
		return receivedOrdsQueue.size();
	}

	// TODO
	private boolean validateOrd(Message msg) {
		return true;
	}

	public void onMessage(NewOrderSingle message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {

		// TODO Validate message with Account persistence
		// If message is rejected return an exec report with OrdStatus=REJECTED
		// Assuming a valid message:
		//
		//
		if (validateOrd(message)) {
			receivedOrdsQueue.add(message);
			if (listenerSet) {
				ordListener.triggerOrdEvent(new Event(this, seqNr, message));
				seqNr++;
			}
		}
		// Return an exec report to sender
		// Need to add some IDs
		// TODO add correct Exec report
		OrderID ordId = new OrderID("1");
		ExecID execId = new ExecID("1");

		ExecType execType = new ExecType(ExecType.NEW);
		OrdStatus ordStatus = new OrdStatus(OrdStatus.PENDING_NEW);
		Side side = message.getSide();
		OrderQty orderQty = message.getOrderQty();
		LeavesQty leavesQty = new LeavesQty(orderQty.getValue());
		CumQty cumQty = new CumQty(0);
		AvgPx avgPx = new AvgPx(0);

		ExecutionReport executionReport = new ExecutionReport(ordId, execId,
				execType, ordStatus, side, leavesQty, cumQty, avgPx);
		executionReport.set(message.getSymbol());
		try {
			Session.sendToTarget(executionReport, sessionID);
		} catch (SessionNotFound e) {
			e.printStackTrace();
		}
	}
}
