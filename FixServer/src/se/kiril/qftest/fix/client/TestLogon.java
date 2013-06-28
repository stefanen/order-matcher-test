package se.kiril.qftest.fix.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;

import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FieldConvertError;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.RuntimeError;
import quickfix.ScreenLogFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;
import quickfix.SocketInitiator;
import quickfix.field.BeginString;
import quickfix.field.HeartBtInt;
import quickfix.field.ResetSeqNumFlag;
import quickfix.fix42.Logon;
import quickfix.fix42.Message.Header;

public class TestLogon {
	public static void main(String[] args) {
		SocketInitiator socketInitiator = null;
		try {
			SessionSettings sessionSettings = new SessionSettings(
					"/home/krl/projects5/QFTest/QFTest/sessionSettings.txt");
			ApplicationClient applicationClient = new ApplicationClient();
			FileStoreFactory fileStoreFactory = new FileStoreFactory(
					sessionSettings);
			FileLogFactory logFactory = new FileLogFactory(sessionSettings);
			MessageFactory messageFactory = new DefaultMessageFactory();
			socketInitiator = new SocketInitiator(applicationClient,
					fileStoreFactory, sessionSettings, logFactory,
					messageFactory);
			socketInitiator.start();
			SessionID sessionId = socketInitiator.getSessions().get(0);
			sendLogonRequest(sessionId);
			int i = 0;
			do {
				try {
					Thread.sleep(1000);
					//System.out.println(socketInitiator.isLoggedOn());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				i++;
			} while ((socketInitiator.isLoggedOn()) && (i < 90));
		} catch (ConfigError e) {
			e.printStackTrace();
		} catch (SessionNotFound e) {
			e.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (socketInitiator != null) {
				socketInitiator.stop(true);
			}
		}
	}

	private static void sendLogonRequest(SessionID sessionId)
			throws SessionNotFound {
		Logon logon = new Logon();
		Header header = (Header) logon.getHeader();
		header.setField(new BeginString("FIX.4.2"));
		logon.set(new HeartBtInt(30));
		logon.set(new ResetSeqNumFlag(true));
		boolean sent = Session.sendToTarget(logon, sessionId);
		System.out.println("Logon Message Sent : " + sent);
	}
}