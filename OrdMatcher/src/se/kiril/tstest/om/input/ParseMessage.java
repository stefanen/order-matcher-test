package se.kiril.tstest.om.input;

import se.kiril.tstest.om.enums.OrdType;
import se.kiril.tstest.om.enums.Side;

// message format:
// "symbol side type price quantity user"
public class ParseMessage {
	private char operation;
	private String symbol;
	private Side side;
	private OrdType ordType;
	private double price;
	private int qty;
	private String user;

	public ParseMessage(String lineToParse) {
		String[] lineElements = lineToParse.split(" ");
		operation = lineElements[0].charAt(0);
		symbol = lineElements[1];
		if (lineElements[2].charAt(0) == 'B') {
			side = Side.BID;
		} else {
			side = Side.ASK;
		}

		if (lineElements[3].charAt(0) == '1') {
			ordType = OrdType.MARKET;
		} else if (lineElements[3].charAt(0) == '2') {
			ordType = OrdType.LIMIT;
		} else if (lineElements[3].charAt(0) == '3') {
			ordType = OrdType.STOP_LOSS;
		} else if (lineElements[3].charAt(0) == '4') {
			ordType = OrdType.STOP_LIMIT;
		} else if (lineElements[3].charAt(0) == 'A') {
			ordType = OrdType.ALL_OR_NOTHING;
		} else if (lineElements[3].charAt(0) == 'F') {
			ordType = OrdType.FILL_OR_KILL;
		} else if (lineElements[3].charAt(0) == 'P') {
			ordType = OrdType.PEGGED;
		} else {
			ordType = OrdType.LIMIT;
		}

		price = Double.parseDouble(lineElements[4]);
		qty = Integer.parseInt(lineElements[5]);
		user = lineElements[6];
	}

	public char getOperation() {
		return operation;
	}

	public String getSymbol() {
		return symbol;
	}

	public Side getSide() {
		return side;
	}

	public OrdType getOrdType() {
		return ordType;
	}

	public double getPrice() {
		return price;
	}

	public int getQty() {
		return qty;
	}

	public String getUser() {
		return user;
	}
}