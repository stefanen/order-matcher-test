package se.kiril.tstest.fs.session;

import quickfix.Message;

public interface ConnectionFixIf {
	public void sendMsg (Message msg);
	public String generateId ();
	public void connect();
	public void disconnect();
	public void listen();
}
