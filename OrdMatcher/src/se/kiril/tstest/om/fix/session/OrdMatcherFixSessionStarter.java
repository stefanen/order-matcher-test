package se.kiril.tstest.om.fix.session;


public class OrdMatcherFixSessionStarter {
	public static void main(String[] args) {
		ConnectionFix matcherFixConnection = new ConnectionFix();
		matcherFixConnection.acceptorConnectAndListen();
	}
}
