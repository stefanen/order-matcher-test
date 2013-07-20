package se.kiril.tstest.om.fix.session;

import quickfix.Message;

public interface ConnectionFixIf {
	
	public void acceptorConnectAndListen();
	public void acceptorDisconnect();
	
	public void initiatorConnectAndLogon();
	public void initiatorDisconnect();
	public void initiatorSendMsg(Message msg);

}
