package se.kiril.tstest.om.fix.session;

import java.util.LinkedList;

import quickfix.Acceptor;
import quickfix.Application;
import quickfix.DefaultMessageFactory;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Initiator;
import quickfix.LogFactory;
import quickfix.MemoryStoreFactory;
import quickfix.Message;
import quickfix.MessageStoreFactory;
import quickfix.RejectLogon;
import quickfix.ScreenLogFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;
import quickfix.SocketInitiator;
import quickfix.ThreadedSocketAcceptor;
import quickfix.ThreadedSocketInitiator;
import quickfix.UnsupportedMessageType;

import quickfix.fix44.NewOrderSingle;

public class ConnectionFix implements ConnectionFixIf, Application{
	
	private Initiator initiator;
	private Acceptor acceptor;
	
	
	
	private String fixAcceptorSettings ="FixAcceptorSettings.cfg";
	private String fixInitiatorSettings ="FixInitiatorSettings.cfg";
	
	protected LinkedList<NewOrderSingle> ordersQueue = new LinkedList<NewOrderSingle>();

	@Override
	public void fromAdmin(Message msg, SessionID sID) throws FieldNotFound,
			IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		System.out.println(msg);
	}

	@Override
	public void fromApp(Message msg, SessionID sID) throws FieldNotFound,
			IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCreate(SessionID sID) {
		System.out.println("New session created with SessionID = " +sID);
		System.out.println("Number of active sessions = " + Session.numSessions());
	}

	@Override
	public void onLogon(SessionID sID) {
		System.out.println("LogON on session " +sID + " ...");
	}

	@Override
	public void onLogout(SessionID sID) {
		System.out.println("LogOUT on session " +sID + " ...");
	}

	@Override
	public void toAdmin(Message msg, SessionID sID) {
		System.out.println(msg);
	}

	@Override
	public void toApp(Message msg, SessionID sID) throws DoNotSend {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acceptorConnectAndListen() {
		acceptor = createAcceptor();
		try{
			acceptor.start();
		}catch (Exception e) {
			e.printStackTrace();
            throw new RuntimeException(e);
        }
	}

	@Override
	public void acceptorDisconnect() {
		try{
			acceptor.stop();
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void initiatorConnectAndLogon() {
		initiator = createInitiator();
		try{
			initiator.start();
			SessionID sessionId = initiator.getSessions().get(0);
			Session.lookupSession(sessionId).logon();
			while(!Session.lookupSession(sessionId).isLoggedOn()){
				System.out.println("Waiting for login success on session "+sessionId);
				Thread.sleep(1000);
			}
			System.out.println("Successfully logged on...");
			Thread.sleep(500);	
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void initiatorSendMsg(Message msg) {
		//Making sure the initiator is set and logged on
		if (initiator != null && Session.lookupSession(initiator.getSessions().get(0)).isLoggedOn()){
			SessionID sessionId = initiator.getSessions().get(0);
			try {
	            Session.sendToTarget(msg, sessionId);
	        } catch (SessionNotFound e) {
	            e.printStackTrace();
	        }
		}else{
			System.err.println("Cannot send message - Initiator is not logged on or hasnt been set yet.");
		}
	}

	@Override
	public void initiatorDisconnect() {
		try{
			initiator.stop();
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private Initiator createInitiator(){
		Initiator init = null;
		try{
			//Session settings from file
			SessionSettings initSessionSettings = new SessionSettings(fixInitiatorSettings); 
			System.out.println("Using session setting for the INITIATOR from: "+ fixInitiatorSettings);
			//Message store factory in memory
			MessageStoreFactory messageStoreFactory = new MemoryStoreFactory();
			//Message factory
			DefaultMessageFactory messageFactory = new DefaultMessageFactory();
			//Logging factory and setting log level here
			LogFactory logFactory = new ScreenLogFactory(true, true, true, true);
			init = new ThreadedSocketInitiator(this, messageStoreFactory, initSessionSettings, logFactory, messageFactory);
		}catch (Exception e){
			e.printStackTrace();
		}
		return init;
	}
	
	private Acceptor createAcceptor(){
		Acceptor acc = null;
		try {
			//Session settings from file
			SessionSettings accSessionSettings = new SessionSettings(fixAcceptorSettings);
			System.out.println("Using session settings for the ACCEPTOR from: "+ fixAcceptorSettings);
			//Message store factory in memory
			MessageStoreFactory messageStoreFactory = new MemoryStoreFactory();
			//Message factory
			DefaultMessageFactory messageFactory = new DefaultMessageFactory();
			//Logging factory and setting log level here
			LogFactory logFactory = new ScreenLogFactory(true, true, true, true);
			//Creating the threaded acceptor
			acc = new ThreadedSocketAcceptor(this, messageStoreFactory, accSessionSettings, logFactory, messageFactory);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return acc;
	}
}