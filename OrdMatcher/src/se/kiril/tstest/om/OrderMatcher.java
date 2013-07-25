package se.kiril.tstest.om;

import java.awt.Event;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Calendar;

import se.kiril.tstest.om.guis.OrderbookSnapshots;
import se.kiril.tstest.om.multicast.MulticastListener;
import se.kiril.tstest.om.multicast.MulticastListenerIf;
import se.kiril.tstest.om.orderbook.Order;
import se.kiril.tstest.om.orderbook.OrderBook;

public class OrderMatcher {
	private static OrderbookSnapshots snapshotsGui = null;
	private static OrderBook obSnapshot = new OrderBook();
	private static Long snapshotTime = null;

	private static OrderBook ob = new OrderBook();
	public static MulticastListener incomingOrdsListener = null;
	private static final String ORDS_MULTICAST_ADDR = "224.223.123.53";
	private static final int ORDS_MULTICAST_PORT = 8888;
	@SuppressWarnings("unused")
	private static final String EXEC_MULTICAST_ADDR = "224.223.123.54";
	@SuppressWarnings("unused")
	private static final int EXEC_MULTICAST_PORT = 8887;

	public static void main(String[] args) {

		// Order testOrder = new Order("GOOG", '1', '2', 13.24, 15.0,
		// "testUsr");
		// ob.addOrder(testOrder);
		// Order testOrder2 = new Order("GOOG", '2', '2', 7.15, 30.0,
		// "testUsr2");
		// ob.addOrder(testOrder2);

		multicastsThread.start();
		snapshotsThread.start();
	}

	private static Thread multicastsThread = new Thread(new Runnable() {
		public void run() {
			System.out.println("Incoming orders multicast group: "
					+ ORDS_MULTICAST_ADDR + ":" + ORDS_MULTICAST_PORT);
			try {
				incomingOrdsListener = new MulticastListener(
						ORDS_MULTICAST_ADDR, ORDS_MULTICAST_PORT);
				incomingOrdsListener.setEventListener(mtxEventListener);
				incomingOrdsListener.listen();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	});

	private static Thread snapshotsThread = new Thread(new Runnable() {
		public void run() {
			try {
				Thread.sleep(100);
				// create gui
				snapshotsGui = new OrderbookSnapshots();
				snapshotsGui.setEventListener(saveObStateEvent);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	});

	private static MulticastListenerIf mtxEventListener = new MulticastListenerIf() {
		@Override
		public void triggerMtxEvent(Event e) {
			processQueues();
		}
	};
	private static SaveObStateEventIf saveObStateEvent = new SaveObStateEventIf() {
		@Override
		public void triggerSaveStateEvent(Event e) {
			try {
				saveOrderbookSnapshot();
				// updateGui();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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

	// This is not very thread safe but it will do the job
	protected static void saveOrderbookSnapshot() throws InterruptedException,
			IOException, ClassNotFoundException {
		// serializing ob
		OrderBook tOb = ob;

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(tOb);
		oos.flush();
		oos.close();
		bos.close();
		byte[] byteData = bos.toByteArray();

		snapshotTime = createTimestamp();
		snapshotsGui.addSnapshotToList(snapshotTime, byteData);
	}

	private static long createTimestamp() {
		Calendar c = Calendar.getInstance();
		long now = c.getTimeInMillis();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		long msSinceMidnight = now - c.getTimeInMillis();
		return msSinceMidnight;
	}
}
