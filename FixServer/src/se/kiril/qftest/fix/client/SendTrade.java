package se.kiril.qftest.fix.client;

import java.io.IOException;
import java.util.Scanner;
 
import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.MessageFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.field.ClOrdID;
import quickfix.field.HandlInst;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.TransactTime;
import quickfix.fix42.NewOrderSingle;
public class SendTrade {
	public static void main(String[] args){
		SocketInitiator socketInitiator = null;
		try{
			SessionSettings initiatorSettings = new SessionSettings(
					"/home/krl/projects5/QFTest/QFTest/sessionSettings.txt");
			ApplicationClient initiatorApplication = new ApplicationClient();
			FileStoreFactory fileStoreFactory = new FileStoreFactory(initiatorSettings);
			FileLogFactory fileLogFactory = new FileLogFactory(initiatorSettings);
			MessageFactory messageFactory = new DefaultMessageFactory();
			
			socketInitiator = new SocketInitiator(initiatorApplication, fileStoreFactory, 
					initiatorSettings, fileLogFactory, messageFactory);
			socketInitiator.start();
			
			SessionID sessionId = socketInitiator.getSessions().get(0);
			Session.lookupSession(sessionId).logon();
			
			while(!Session.lookupSession(sessionId).isLoggedOn()){
				System.out.println("Waiting for login success...");
				Thread.sleep(1000);
			}
			System.out.println("Logged in!");
			
			Thread.sleep(5000);
			bookSingleOrder(sessionId);
			
			System.out.println("Press any key to exit...");
			Scanner scanner = new Scanner(System.in);
			scanner.next();
			Session.lookupSession(sessionId).disconnect("Done",false);
			socketInitiator.stop();
			
		} catch (ConfigError e){
			e.printStackTrace();
		} catch (InterruptedException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private static void bookSingleOrder(SessionID sessionID){
		ClOrdID orderId = new ClOrdID("1");
		HandlInst instruction = new HandlInst(HandlInst.AUTOMATED_EXECUTION_ORDER_PRIVATE);
		Symbol mainCurrency = new Symbol("EUR/USD");
		Side side = new Side(Side.BUY);
		TransactTime transactionTime = new TransactTime();
		OrdType orderType = new OrdType(OrdType.FOREX_MARKET);
		
		NewOrderSingle newOrderSingle = new NewOrderSingle(orderId, instruction, 
					mainCurrency, side, transactionTime, orderType);
		
		newOrderSingle.set(new OrderQty(100));
		
		try{
			Session.sendToTarget(newOrderSingle, sessionID);
		} catch (SessionNotFound e){
			e.printStackTrace();
		}
		
	}
}
