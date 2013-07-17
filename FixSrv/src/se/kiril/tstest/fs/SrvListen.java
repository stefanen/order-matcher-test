package se.kiril.tstest.fs;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;
import quickfix.fix44.MessageFactory;
import se.kiril.tstest.fs.commons.FixOrder;

public class SrvListen {
	
	public static void main(String[] args) {
        SocketAcceptor socketAcceptor = null;
        try {
            SessionSettings serverFixSettings = new SessionSettings("ServerFixSettings.txt");
            Application application = new ServerApplicationImpl();
            FileStoreFactory fileStoreFactory = new FileStoreFactory(serverFixSettings);
            DefaultMessageFactory messageFactory = new DefaultMessageFactory();
            FileLogFactory fileLogFactory = new FileLogFactory(serverFixSettings);
            socketAcceptor = new SocketAcceptor(application, fileStoreFactory,
                    serverFixSettings, fileLogFactory, messageFactory);
            socketAcceptor.start();
        } catch (ConfigError e) {
            e.printStackTrace();
        }
        
    }
}
