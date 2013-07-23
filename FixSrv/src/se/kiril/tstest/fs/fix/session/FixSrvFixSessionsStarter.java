package se.kiril.tstest.fs.fix.session;

import java.awt.Event;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import quickfix.field.CheckSum;
import quickfix.fix44.Message;
import quickfix.fix44.NewOrderSingle;
import se.kiril.tstest.fs.commons.OrderListener;

public class FixSrvFixSessionsStarter {

	private static FsConnectionFix fixAccConnection = new FsConnectionFix();
	
	private static final String MULTICAST_ADDR = "224.2.2.3";
	private static final int MULTICAST_PORT = 8888;
	private static InetAddress address = null; 
	private static DatagramSocket socket = null;


	public static void main(String[] args) throws SocketException, UnknownHostException{
		address = InetAddress.getByName(MULTICAST_ADDR);
		socket = new DatagramSocket();
		
		fixAccConnection.acceptorConnectAndListen("FixSrvFixAcceptorSettings.cfg");
		processQueues();

		OrderListener ordListener = new OrderListener() {
			@Override
			public void triggerOrdEvent(Event e) {
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

			bdxFixMessage(ordToRelay);
			
			if (fixAccConnection.getQueueSize() > 0) {
				processQueues();
			}
		}
	}

	static void bdxFixMessage(Message pMsg){
		DatagramPacket outPacket = null;
		byte[] outBuf;
		try{
			Message fixMsg = pMsg;
			//Removing SenderCompID - its not needed since this a multicast message
			//Might as well make it smaller
			fixMsg.getHeader().removeField(49);
			//Removing the sending time - This is the client sending time and its not needed here as we have 
			//TransactTime (Field 60)
			fixMsg.getHeader().removeField(52);
			//Removing TargetCompID - not needed anymore
			fixMsg.getHeader().removeField(56);
			//Resetting the checksum
			fixMsg.set(new CheckSum());
		
			String msg = fixMsg.toString();
			outBuf = msg.getBytes();
			outPacket = new DatagramPacket(outBuf, outBuf.length, address, MULTICAST_PORT);
			socket.send(outPacket);
			System.out.println("Broadcasting: " + msg);
		} catch (IOException e){
			e.printStackTrace();
		} 
	}

}