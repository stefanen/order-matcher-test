package se.kiril.tstest.om.multicast;

import java.awt.Event;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.LinkedList;

public class MulticastListener {

	private MulticastListenerIf mtxListener;
	private static volatile int eventNr = 1;
	protected LinkedList<String> msgBuffer = new LinkedList<String>();

	private static MulticastSocket socket = null;
	private static DatagramPacket inPacket = null;

	public MulticastListener(String multicastAddr, int multicastPort)
			throws IOException {
		InetAddress address = InetAddress.getByName(multicastAddr);
		socket = new MulticastSocket(multicastPort);
		socket.joinGroup(address);
	}

	public void listen() throws IOException {
		byte[] inBuf = new byte[256];
		System.out.println("Listening for multicasts (35=D)...");
		while (true) {
			inPacket = new DatagramPacket(inBuf, inBuf.length);
			socket.receive(inPacket);
			String msg = new String(inBuf, 0, inPacket.getLength());
			if (msg.contains("35=D")) {
				msgBuffer.add(msg);
				mtxListener.triggerMtxEvent(new Event(this, eventNr, msg));
				eventNr++;
			}
		}
	}

	public void setEventListener(MulticastListenerIf lst) {
		this.mtxListener = lst;
	}

	public int getQueueSize() {
		return msgBuffer.size();
	}

	public String getOldestInQueue() {
		return msgBuffer.getFirst();
	}

	public void removeOldestInQueue() {
		msgBuffer.removeFirst();
	}
}
