package se.kiril.qftest.fix.server;
class TestMarketData {
	public static void main(String args[]) throws InterruptedException {
		MarketData md = new MarketData();
		while (true) {
			String ask = md.getBid("0001");
			System.out.println(ask);
			Thread.sleep(1000);
		}
	}
}