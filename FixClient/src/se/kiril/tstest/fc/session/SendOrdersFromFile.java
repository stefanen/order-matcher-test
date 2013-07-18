package se.kiril.tstest.fc.session;

import java.awt.List;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.Dictionary;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.LogFactory;
import quickfix.ScreenLogFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.field.Account;
import quickfix.field.BeginString;
import quickfix.field.ClOrdID;
import quickfix.field.HandlInst;
import quickfix.field.HeartBtInt;
import quickfix.field.NewPassword;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.field.ResetSeqNumFlag;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.TransactTime;
import quickfix.fix44.Logon;
import quickfix.fix44.Message.Header;
import quickfix.fix44.MessageFactory;
import quickfix.fix44.NewOrderSingle;
import se.kiril.tstest.fc.commons.FixOrder;
import se.kiril.tstest.fc.fileinput.ParseFile;
import se.kiril.tstest.fc.fileinput.ParseMessage;

public class SendOrdersFromFile {
	public static void main(String[] args) throws IOException { 
		String tradesFile = "trades.in";
		SocketInitiator socketInitiator = null;
		List fLines = new List();
		ParseFile pf = new ParseFile(tradesFile);
		fLines = pf.getParsedFile();
		System.out.println("Loaded trades from" + tradesFile);
		try {
			SessionSettings clientFixSettings = new SessionSettings("FixInitiatorSettings.cfg");
						
			Application initiatorApplication = new ClientApplicationImpl();
			FileStoreFactory fileStoreFactory = new FileStoreFactory(
					clientFixSettings);
//			FileLogFactory fileLogFactory = new FileLogFactory(clientFixSettings);
			LogFactory fileLogFactory = new ScreenLogFactory(true, false, false, false);
			DefaultMessageFactory messageFactory = new DefaultMessageFactory();
			socketInitiator = new SocketInitiator(initiatorApplication, fileStoreFactory, clientFixSettings, fileLogFactory, messageFactory);
			socketInitiator.start();
			SessionID sessionId = socketInitiator.getSessions().get(0);
			Session.lookupSession(sessionId).logon();
			while(!Session.lookupSession(sessionId).isLoggedOn()){
				System.out.println("Waiting for login success");
				Thread.sleep(1000);
			}
			System.out.println("Logged In...");

			Thread.sleep(1000);
			//
			// Looping though the trade lines from the file input
			for (int i = 0; i < fLines.getItemCount(); i++) {
				ParseMessage pm = new ParseMessage(fLines.getItem(i));
				FixOrder fOrd = new FixOrder(pm.getSymbol(), pm.getSide(), 
						pm.getOrdType(), pm.getPrice(), pm.getQty(), pm.getUser());
				sendSingleOrder(sessionId, fOrd);
				//Thread.sleep(2);
			}
//			System.out.println("Type to quit");
//			Scanner scanner = new Scanner(System.in);
//			scanner.next();
//			Session.lookupSession(sessionId).disconnect("Done",false);
			socketInitiator.stop();
		} catch (ConfigError e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
	}
	
	private static void sendSingleOrder(SessionID sessionID, FixOrder ord){
        //HandlInst instruction = new HandlInst(HandlInst.AUTOMATED_EXECUTION_ORDER_PRIVATE);
		ClOrdID orderId = new ClOrdID(ord.getOrdSeqNr().toString());
        Side side = new Side(ord.getSide());
        TransactTime transactionTime = new TransactTime();
        OrdType orderType = new OrdType(OrdType.LIMIT);
        
        NewOrderSingle newOrderSingle = new NewOrderSingle(orderId,side,transactionTime,orderType);
        
        newOrderSingle.set(new HandlInst(HandlInst.AUTOMATED_EXECUTION_ORDER_PRIVATE));
        newOrderSingle.set(new OrderQty(ord.getQty()));
        newOrderSingle.set(new Symbol(ord.getSymbol()));
        newOrderSingle.set(new Price(ord.getLimitPx()));
        newOrderSingle.set(new Account(ord.getUser()));
      
        try {
            Session.sendToTarget(newOrderSingle, sessionID);
        } catch (SessionNotFound e) {
            e.printStackTrace();
        }
    }
}

