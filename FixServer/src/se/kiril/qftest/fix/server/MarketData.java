package se.kiril.qftest.fix.server;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MarketData {
	private static final int maxPrice = 500;
	private Random generator;
	private HashMap<String, Object> bids;
	private HashMap<String, Object> asks;

	MarketData() {
		bids = new HashMap<String, Object>();
		asks = new HashMap<String, Object>();
		generator = new Random();

		// Generate some random symbols
		Integer basePrice;
		Double decimal, price;
		String symbol, bid_price, ask_price;
		for (int index = 1; index < 1000; index++) {
			basePrice = generator.nextInt(maxPrice);
			decimal = generator.nextDouble();
			price = basePrice.doubleValue() + decimal;
			symbol = String.format("%04d", index);
			bid_price = String.format("%.2f", price);
			ask_price = String.format("%.2f", price + 0.01);
			bids.put(symbol, bid_price);
			asks.put(symbol, ask_price);
		}

		// Update market data every 200 ms
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				update();
			}
		};
		timer.scheduleAtFixedRate(task, 0, 200);

	}

	public String getBid(String symbol) {
		return (String) bids.get(symbol);
	}

	public String getAsk(String symbol) {
		return (String) asks.get(symbol);
	}

	public void update() {
		Double current_bid, current_ask, new_bid, new_ask;
		Double delta, price;
		Integer up_dn;
		String current_key;

		for (Map.Entry<String, Object> entry : bids.entrySet()) {
			current_key = (String) entry.getKey();
			current_bid = Double.parseDouble((String) bids.get(current_key));
			current_ask = Double.parseDouble((String) asks.get(current_key));

			delta = generator.nextDouble();
			up_dn = generator.nextInt() % 2;
			delta = up_dn.doubleValue() * delta;
			new_bid = current_bid + delta;
			new_ask = new_bid + 0.1;

			bids.put(current_key, String.format("%.2f", new_bid));
			asks.put(current_key, String.format("%.2f", new_ask));
		}
	}
}