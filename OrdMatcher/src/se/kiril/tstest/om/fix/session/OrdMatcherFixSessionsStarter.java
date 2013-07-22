package se.kiril.tstest.om.fix.session;

import java.awt.Event;

import quickfix.FieldNotFound;
import quickfix.fix44.NewOrderSingle;
import se.kiril.tstest.fs.commons.OrderListener;
import se.kiril.tstest.om.orderbook.Order;
import se.kiril.tstest.om.orderbook.OrderBook;

//import se.kiril.tstest.om.orderbook.OrderBook;

public class OrdMatcherFixSessionsStarter {
	private static OmConnectionFix fixAccConnection = new OmConnectionFix();

	private static OrderBook ob = new OrderBook();

	public static void main(String[] args) {

		fixAccConnection
				.acceptorConnectAndListen("OrdMatcherFixAcceptorSettings.cfg");
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
			NewOrderSingle ordToInsert = new NewOrderSingle();
			ordToInsert = fixAccConnection.getOldestInQueue();
			fixAccConnection.removeOldestInQueue();

			Order newOrd = null;
			try {
				newOrd = new Order(ordToInsert.getSymbol().getValue(),
						ordToInsert.getSide().getValue(), ordToInsert
								.getOrdType().getValue(), ordToInsert
								.getPrice().getValue(), ordToInsert
								.getOrderQty().getValue(), ordToInsert
								.getAccount().getValue());
			} catch (FieldNotFound e) {
				e.printStackTrace();
				System.err.println("Error while inserting trade to Orderbook!");
			}
			// printExecReports(ob.addOrder(newOrd));
			ob.addOrder(newOrd);
			// fixAccConnection.
			ob.execOrder(newOrd);

			if (fixAccConnection.getQueueSize() > 0) {
				processQueues();
			}
		}
	}
}
