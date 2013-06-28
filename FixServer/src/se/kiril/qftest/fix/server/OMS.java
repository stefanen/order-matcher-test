package se.kiril.qftest.fix.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;

import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FieldConvertError;
import quickfix.FileStoreFactory;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.RuntimeError;
import quickfix.ScreenLogFactory;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;
// Quickfix Exceptions
// Quickfix Data Types

public class OMS {
	private final SocketAcceptor acceptor;

	public OMS(SessionSettings settings) throws ConfigError, FieldConvertError {
		Application app = new Application(settings);
		MessageStoreFactory msgStoreFactory = new FileStoreFactory(settings);
		LogFactory logFactory = new ScreenLogFactory(true, true, true);
		MessageFactory msgFactory = new DefaultMessageFactory();

		acceptor = new SocketAcceptor(app, msgStoreFactory, settings,
				logFactory, msgFactory);
	}

	// Accept incoming connections
	public void start() throws RuntimeError, ConfigError {
		acceptor.start();
	}

	// Stop accepting incoming connections
	public void stop() {
		acceptor.stop();
	}

	// Program entry-point
	public static void main(String args[]) {
		SessionSettings sessionSettings = null;
		try {
			if (args.length != 1) {
				System.err.println(" Usage: OMS ");
			}
			sessionSettings = loadConfiguration(args[0]);
			Iterator<SessionID> sectionIterator = sessionSettings
					.sectionIterator();
			while (sectionIterator.hasNext()) {
				SessionID id = sectionIterator.next();
				System.out.println(id.toString());
			}

			OMS oms = new OMS(sessionSettings);
			oms.start();
			System.out.println("press q to quit");
			System.in.read();
			oms.stop();
		} catch (Exception ex) {
			System.err.println("Exception!");
			System.err.println(ex.getMessage());
		}
		System.exit(0);
	}

	// Load customer configuration from first argument
	private static SessionSettings loadConfiguration(String configFilePath)
			throws FileNotFoundException {
		SessionSettings settings = null;
		try {
			InputStream inputStream = new FileInputStream(configFilePath);
			settings = new SessionSettings(inputStream);
			inputStream.close();
		} catch (Exception ex) {
			System.err.println(ex.getMessage());

		}
		return settings;
	}

}