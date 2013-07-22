package se.kiril.tstest.fs.fix.connection;

import quickfix.Message;

public interface ConnectionFixIf {

	public void acceptorConnectAndListen(String fixSettings);

	public void acceptorDisconnect();

	public void initiatorConnectAndLogon(String fixSettings);

	public void initiatorDisconnect();

	public void initiatorSendMsg(Message msg);
}
