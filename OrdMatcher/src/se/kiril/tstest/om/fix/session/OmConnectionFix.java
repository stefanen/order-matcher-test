package se.kiril.tstest.om.fix.session;

import java.awt.Event;
import java.util.LinkedList;

import quickfix.FieldNotFound;
import quickfix.IncorrectTagValue;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.fix44.NewOrderSingle;
import se.kiril.tstest.fs.commons.OrderListener;
import se.kiril.tstest.fs.fix.connection.ConnectionFix;

public class OmConnectionFix extends ConnectionFix {

	private static volatile int seqNr = 1;
	private boolean listenerSet = false;
	private OrderListener ordListener;
	protected LinkedList<NewOrderSingle> receivedOrdsQueue = new LinkedList<NewOrderSingle>();

	public void setListener(OrderListener lst) {
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

	public void onMessage(NewOrderSingle message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {

		// TODO Validate message with Account persistence
		// If message is rejected return an exec report with OrdStatus=REJECTED
		// Assuming a valid message:
		//
		//
		receivedOrdsQueue.add(message);
		if (listenerSet) {
			ordListener.triggerOrdEvent(new Event(this, seqNr, message));
			seqNr++;
		}
	}
}
