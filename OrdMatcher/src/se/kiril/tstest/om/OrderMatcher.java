package se.kiril.tstest.om;

import java.awt.Event;
import java.io.IOException;

import se.kiril.tstest.om.multicast.MulticastListener;
import se.kiril.tstest.om.multicast.MulticastListenerIf;
import se.kiril.tstest.om.orderbook.Order;
import se.kiril.tstest.om.orderbook.OrderBook;

public class OrderMatcher {
	private static OrderBook ob = new OrderBook();
	public static MulticastListener incomingOrdsListener = null;
	private static final String ORDS_MULTICAST_ADDR = "224.223.123.53";
	private static final int ORDS_MULTICAST_PORT = 8888;
	@SuppressWarnings("unused")
	private static final String EXEC_MULTICAST_ADDR = "224.223.123.54";
	@SuppressWarnings("unused")
	private static final int EXEC_MULTICAST_PORT = 8887;

	public static void main(String[] args) throws IOException {
		System.out.println("Incoming orders multicast group: "
				+ ORDS_MULTICAST_ADDR + ":" + ORDS_MULTICAST_PORT);
		incomingOrdsListener = new MulticastListener(ORDS_MULTICAST_ADDR,
				ORDS_MULTICAST_PORT);
		incomingOrdsListener.setEventListener(mtxEventListener);
		incomingOrdsListener.listen();
	}

	private static MulticastListenerIf mtxEventListener = new MulticastListenerIf() {
		@Override
		public void triggerMtxEvent(Event e) {
			processQueues();
		}
	};

	private static void processQueues() {
		if (incomingOrdsListener.getQueueSize() > 0) {
			String tOrd = incomingOrdsListener.getOldestInQueue();
			incomingOrdsListener.removeOldestInQueue();

			Order ord = parseFixToOrder(tOrd);

			ob.addOrder(ord);
			ob.execOrder(ord);

			if (incomingOrdsListener.getQueueSize() > 0) {
				processQueues();
			}
		}
	}

	/**
	 * Some crude fix parsing
	 * 
	 * @param fixStringOrd
	 * @return Order
	 */
	private static Order parseFixToOrder(String fixStringOrd) {
		String[] tOrdParsed = fixStringOrd.split("\\x01");
		Order newOrd = new Order(tOrdParsed[12].split("=")[1],
				tOrdParsed[11].split("=")[1].charAt(0),
				tOrdParsed[9].split("=")[1].charAt(0),
				Double.parseDouble(tOrdParsed[10].split("=")[1]),
				Double.parseDouble(tOrdParsed[8].split("=")[1]),
				tOrdParsed[4].split("=")[1]);
		return newOrd;
	}
}
