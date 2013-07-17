package se.kiril.tstest.fc.fileinput;

import quickfix.field.OrdType;
import quickfix.field.Side;



// message format:
// "symbol side type price quantity user"
public class ParseMessage {
	private char operation;
	private String symbol;
	private char side;
	private char ordType;
	private double price;
	private int qty;
	private String user;

	public ParseMessage(String lineToParse) {
		String[] lineElements = lineToParse.split(" ");
		operation = lineElements[0].charAt(0);
		symbol = lineElements[1];
		if (lineElements[2].charAt(0) == 'B') {
			side = Side.BUY;
		} else {
			side = Side.SELL;
		}

		if (lineElements[3].charAt(0) == '1') {
			ordType = OrdType.MARKET;
		} else if (lineElements[3].charAt(0) == '2') {
			ordType = OrdType.LIMIT;
		} else if (lineElements[3].charAt(0) == '3') {
			ordType = OrdType.STOP;
		} else if (lineElements[3].charAt(0) == '4') {
			ordType = OrdType.STOP_LIMIT;
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

	public char getSide() {
		return side;
	}

	public char getOrdType() {
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