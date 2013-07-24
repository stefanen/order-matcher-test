package se.kiril.tstest.fs;

import java.awt.Event;
import java.net.SocketException;
import java.net.UnknownHostException;

import quickfix.fix44.NewOrderSingle;
import se.kiril.tstest.fs.commons.OrderListenerIf;
import se.kiril.tstest.fs.fix.session.FsConnectionFix;
import se.kiril.tstest.fs.multicast.MsgBroadcaster;

public class FixServer {
	private static FsConnectionFix fixAccConnection = new FsConnectionFix();
	private static MsgBroadcaster ordBroadcaster = null;
	private static final String MULTICAST_ADDR = "224.223.123.53";
	private static final int MULTICAST_PORT = 8888;

	public static void main(String[] args) throws SocketException,
			UnknownHostException {
		ordBroadcaster = new MsgBroadcaster(MULTICAST_ADDR, MULTICAST_PORT);
		fixAccConnection
				.acceptorConnectAndListen("FixSrvFixAcceptorSettings.cfg");
		processQueues();
		fixAccConnection.setListener(fixOrdEventListener);
	}

	private static OrderListenerIf fixOrdEventListener = new OrderListenerIf() {
		@Override
		public void triggerOrdEvent(Event e) {
			processQueues();
		}
	};

	static void processQueues() {
		if (fixAccConnection.getQueueSize() > 0) {
			NewOrderSingle ordToRelay = new NewOrderSingle();
			ordToRelay = fixAccConnection.getOldestInQueue();
			fixAccConnection.removeOldestInQueue();
			ordBroadcaster.bdxFixMessage(ordToRelay);
			if (fixAccConnection.getQueueSize() > 0) {
				processQueues();
			}
		}
	}
}
