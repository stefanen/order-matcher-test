package se.kiril.qftest.fix.client;
import java.io.*;
import java.util.*;

// Quickfix Exceptions
import quickfix.DoNotSend;
import quickfix.FieldConvertError;
import quickfix.ConfigError;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.FieldNotFound;
import quickfix.UnsupportedMessageType;
import quickfix.SessionNotFound;
import quickfix.RejectLogon;

// Quickfix Data Types
import quickfix.fix42.ExecutionReport;
import quickfix.SessionID;
import quickfix.Message;
import quickfix.Session;
import quickfix.SessionSettings;
import quickfix.FixVersions;
import quickfix.MessageUtils;
import quickfix.field.*;
public class ApplicationClient extends quickfix.MessageCracker implements quickfix.Application {
    @Override
    public void fromAdmin(Message arg0, SessionID arg1) throws FieldNotFound,
            IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        System.out.println("Successfully called fromAdmin for sessionId : "
                + arg0);
    }
 
    @Override
    public void fromApp(Message arg0, SessionID arg1) throws FieldNotFound,
            IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        System.out.println("Successfully called fromApp for sessionId : "
                + arg0);
        crack(arg0, arg1);
    }
 
    @Override
    public void onCreate(SessionID arg0) {
        System.out.println("Successfully called onCreate for sessionId : "
                + arg0);
    }
 
    @Override
    public void onLogon(SessionID arg0) {
        System.out.println("Successfully logged on for sessionId : " + arg0);
    }
 
    @Override
    public void onLogout(SessionID arg0) {
        System.out.println("Successfully logged out for sessionId : " + arg0);
    }
 
    @Override
    public void toAdmin(Message message, SessionID sessionId) {
        System.out.println("Inside toAdmin");
    }
 
    @Override
    public void toApp(Message arg0, SessionID arg1) throws DoNotSend {
        System.out.println("Message : " + arg0 + " for sessionid : " + arg1);
    }
 
    public void onMessage(quickfix.fix42.NewOrderSingle message, SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        System.out.println("Inside onMessage for New Order Single");
        super.onMessage(message, sessionID);
    }
 
    public void onMessage(quickfix.fix42.SecurityDefinition message, SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        System.out.println("Inside onMessage for SecurityDefinition");
        super.onMessage(message, sessionID);
    }
 
    public void onMessage(quickfix.fix42.Logon message, SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        System.out.println("Inside Logon Message");
        super.onMessage(message, sessionID);
    }
    
    public void onMessage(ExecutionReport message, SessionID sessionID)
    		throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue{
    	System.out.println("========================================");
    	System.out.println("Received Execution report from server");
    	System.out.println("OrderId: "+ message.getOrderID().getValue());
    	System.out.println("Order status: "+ message.getOrdStatus().getValue());
    	System.out.println("Order price: "+ message.getPrice().getValue());
    	System.out.println("========================================");
    }
    
    
}