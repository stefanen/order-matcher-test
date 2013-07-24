package se.kiril.tstest.fs.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import quickfix.field.CheckSum;
import quickfix.fix44.Message;

public class MsgBroadcaster {
	private static InetAddress address = null;
	private static DatagramSocket socket = null;
	private static int MULTICAST_PORT;

	public MsgBroadcaster(String multicastAddr, int multicastPort)
			throws UnknownHostException, SocketException {
		MULTICAST_PORT = multicastPort;
		address = InetAddress.getByName(multicastAddr);
		socket = new DatagramSocket();

	}

	public void bdxFixMessage(Message pMsg) {
		DatagramPacket outPacket = null;
		byte[] outBuf;
		try {
			Message fixMsg = pMsg;
			// Removing SenderCompID - its not needed since this a multicast
			// message
			// Might as well make it smaller
			fixMsg.getHeader().removeField(49);
			// Removing the sending time - This is the client sending time and
			// its not needed here as we have
			// TransactTime (Field 60)
			fixMsg.getHeader().removeField(52);
			// Removing TargetCompID - not needed anymore
			fixMsg.getHeader().removeField(56);
			// Resetting the checksum
			fixMsg.set(new CheckSum());

			String msg = fixMsg.toString();
			outBuf = msg.getBytes();
			outPacket = new DatagramPacket(outBuf, outBuf.length, address,
					MULTICAST_PORT);
			socket.send(outPacket);
			// System.out.println("Broadcasting: " + msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
