package se.kiril.tstest.fs.session;

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
import quickfix.SessionID;
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
	
	private DefaultMessageFactory messageFactory = new DefaultMessageFactory();
	
	private String fixAcceptorSettings ="FixAcceptorSettings.cfg";
	private String fixInitiatorSettings ="FixInitiatorSettings.cfg";
	
	protected LinkedList<NewOrderSingle> ordersQueue = new LinkedList<>();
	
	
	@Override
	public void onCreate(SessionID sessionId) {
		// TODO Auto-generated method stub
		System.out.println("Session created with SessionID = " + sessionId);
	}

	@Override
	public void onLogon(SessionID sessionId) {
		// TODO Auto-generated method stub
		System.out.println("Logon SUCCESS on session " +sessionId);
	}
	@Override
	public void onLogout(SessionID sessionId) {
		// TODO Auto-generated method stub	
		System.out.println("Logout on session " +sessionId);
	}

	@Override
	public void toAdmin(Message message, SessionID sessionId) {
		// TODO Auto-generated method stub
	}

	@Override
	public void fromAdmin(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
			RejectLogon {
		// TODO Auto-generated method stub

	}

	@Override
	public void toApp(Message message, SessionID sessionId) throws DoNotSend {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fromApp(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
			UnsupportedMessageType {
		// TODO Auto-generated method stub
		if (message instanceof NewOrderSingle){
			if (validateOrd(message)){
				queueOrder(message);
			}
		}
	}

	@Override
	public void sendMsg(Message msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String generateId() {
		// TODO Auto-generated method stub
		return null;
	}
	private boolean validateOrd(Message msg){
		return true;
	}
	private void queueOrder(Message msg){
		//not sure if this casting is safe
		ordersQueue.add((NewOrderSingle) msg);
	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
//		initiator = createInitiator();
//		try{
//			initiator.start();
//		}catch (Exception e) {
//            throw new RuntimeException(e);
//        }
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		initiator.stop();
	}
	
//	private Initiator createInitiator() {
//        Initiator initiator = null;
//        SessionSettings fixInitSessionSettings = null;
//        try {
//            fixInitSessionSettings = new SessionSettings(clientFixSettings);
//            
//            MessageStoreFactory messageStoreFactory = new MemoryStoreFactory();
//            // Logging level
//            LogFactory logFactory = new ScreenLogFactory(true, false, false, false);
//            initiator = new ThreadedSocketInitiator(this, messageStoreFactory, fixInitSessionSettings, logFactory, messageFactory);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        return initiator;
//    }
	
	protected Acceptor createAcceptor() {
		Acceptor acceptor = null;
		SessionSettings fixAcceptSessionSettings = null;
		try {
			fixAcceptSessionSettings = new SessionSettings(fixAcceptorSettings);
			MessageStoreFactory messageStoreFactory = new MemoryStoreFactory();
			//Logging level
			LogFactory logFactory = new ScreenLogFactory(true, false, false, false);
//			FileLogFactory logFactory = new FileLogFactory(fixAcceptSessionSettings);
			acceptor = new ThreadedSocketAcceptor(this, messageStoreFactory, fixAcceptSessionSettings, logFactory, messageFactory);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return acceptor;
	}

	@Override
	public void listen() {
		// TODO Auto-generated method stub
		 acceptor = createAcceptor();
		try{
			acceptor.start();
		}catch (Exception e) {
            throw new RuntimeException(e);
        }
		
	}

}
