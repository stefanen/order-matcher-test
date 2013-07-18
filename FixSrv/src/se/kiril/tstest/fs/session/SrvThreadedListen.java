package se.kiril.tstest.fs.session;


public class SrvThreadedListen {
	public static void main(String[] args) {
		ConnectionFix listenConnection = new ConnectionFix();
		listenConnection.listen();
//		listenConnection.startUp():
	}
}
