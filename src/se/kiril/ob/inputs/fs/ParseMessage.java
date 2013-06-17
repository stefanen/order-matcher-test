package se.kiril.ob.inputs.fs;

// message format:
// "symbol side price quantity user"
public class ParseMessage {
	private String symbol;
	private char side;
	private double price;
	private int qty;
	private String user;
	public ParseMessage(String lineToParse){
		String[] lineElements = lineToParse.split(" ");
		symbol = lineElements[0];
		side = lineElements[1].charAt(0);
		price = Double.parseDouble(lineElements[2]);
		qty = Integer.parseInt(lineElements[3]);
		user = lineElements[4];
	}
	public String getSymbol() {	
		return symbol;
	}
	public char getSide() {
		return side;
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
