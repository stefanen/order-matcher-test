package se.kiril.tstest.fc;

import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;	
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.Logon;
import quickfix.fix44.MessageCracker;
import quickfix.fix44.NewOrderSingle;
import quickfix.fix44.SecurityDefinition;

public class ClientApplicationImpl extends MessageCracker implements Application {

	@Override
	public void fromAdmin(Message arg0, SessionID arg1) throws FieldNotFound,
			IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		// TODO Auto-generated method stub
		System.out.println("Successfully called fromAdmin for sessionId : " + arg0);
	}
	@Override
	public void fromApp(Message message, SessionID sessionId) throws FieldNotFound,
			IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		// TODO Auto-generated method stub
		crack(message, sessionId);
	}
	@Override
	public void onCreate(SessionID arg0) {
		// TODO Auto-generated method stub
		System.out.println("Successfully called onCreate for sessionId : " + arg0);
	}
	@Override
	public void onLogon(SessionID arg0) {
		// TODO Auto-generated method stub
		System.out.println("Successfully logged on for sessionId : " + arg0);
	}
	@Override
	public void onLogout(SessionID arg0) {
		// TODO Auto-generated method stub
		System.out.println("Successfully logged out for sessionId : " + arg0);
	}
	@Override
	public void toAdmin(Message message, SessionID sessionId) {
		// TODO Auto-generated method stub
		System.out.println(sessionId + " " +message);
	}
	@Override
	public void toApp(Message message, SessionID sessionId) throws DoNotSend {
		// TODO Auto-generated method stub
		System.out.println(sessionId + ": "+ message);
	}
	@Override
	
	//==========================================================================
	//My messages
	
	public void onMessage(NewOrderSingle message, SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        System.out.println("Inside onMessage for New Order Single");
        super.onMessage(message, sessionID);
    }
 
    @Override
    public void onMessage(SecurityDefinition message, SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        System.out.println("Inside onMessage for SecurityDefinition");
        super.onMessage(message, sessionID);
    }
 
    @Override
    public void onMessage(Logon message, SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        System.out.println("Inside Logon Message");
        super.onMessage(message, sessionID);
    }
    
    public void onMessage(ExecutionReport message, SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
    	System.out.println(sessionID + ": "+ message);
    }
}
