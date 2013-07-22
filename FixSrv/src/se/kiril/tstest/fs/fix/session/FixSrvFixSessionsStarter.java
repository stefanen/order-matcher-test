package se.kiril.tstest.fs.fix.session;

import java.awt.Event;

import quickfix.fix44.NewOrderSingle;
import se.kiril.tstest.fs.commons.OrderListener;

public class FixSrvFixSessionsStarter {

	private static FsConnectionFix fixAccConnection = new FsConnectionFix();
	private static FsConnectionFix fixInitConnection = new FsConnectionFix();

	public static void main(String[] args) {

		fixAccConnection
				.acceptorConnectAndListen("FixSrvFixAcceptorSettings.cfg");
		fixInitConnection
				.initiatorConnectAndLogon("FixSrvFixInitiatorSettings.cfg");
		processQueues();

		OrderListener ordListener = new OrderListener() {

			@Override
			public void triggerOrdEvent(Event e) {
				// System.out.println("Queue size: "+fixAccConnection.getQueueSize());
				processQueues();
			}
		};
		fixAccConnection.setListener(ordListener);
	}

	static void processQueues() {
		if (fixAccConnection.getQueueSize() > 0) {
			NewOrderSingle ordToRelay = new NewOrderSingle();
			ordToRelay = fixAccConnection.getOldestInQueue();
			fixAccConnection.removeOldestInQueue();

			fixInitConnection.initiatorSendMsg(ordToRelay);

			if (fixAccConnection.getQueueSize() > 0) {
				processQueues();
			}
		}
	}

}
